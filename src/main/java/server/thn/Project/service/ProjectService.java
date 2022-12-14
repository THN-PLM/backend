package server.thn.Project.service;

import com.sun.istack.Nullable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import server.thn.Common.dto.MemberIdReq;
import server.thn.Common.dto.response.IdResponse;
import server.thn.Common.entity.EntityDate;
import server.thn.File.repository.AttachmentTagRepository;
import server.thn.File.service.FileService;
import server.thn.Member.entity.Member;
import server.thn.Member.exception.MemberNotFoundException;
import server.thn.Member.repository.MemberRepository;
import server.thn.Project.dto.*;
import server.thn.Project.entity.Project;
import server.thn.Project.entity.ProjectAttachment;
import server.thn.Project.exception.ProjectNotFoundException;
import server.thn.Project.repository.ProjectAttachmentRepository;
import server.thn.Project.repository.ProjectRepository;
import server.thn.Project.repository.ProjectTypeRepository;
import server.thn.Project.repository.buyer.BuyerOrganizationRepository;
import server.thn.Project.repository.carType.CarTypeRepository;
import server.thn.Project.repository.classification.ProduceOrganizationClassification1Repository;
import server.thn.Project.repository.classification.ProduceOrganizationClassification2Repository;
import server.thn.Project.repository.produceOrg.ProduceOrganizationRepository;
import server.thn.Route.entity.RouteOrdering;
import server.thn.Route.repository.RouteOrderingRepository;
import server.thn.Route.repository.RouteProductRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ProjectService {

    private final MemberRepository memberRepository;
    private final ProjectTypeRepository projectTypeRepository;
    private final ProjectRepository projectRepository;
    private final ProduceOrganizationRepository produceOrganizationRepository;
    private final BuyerOrganizationRepository buyerOrganizationRepository;
    private final FileService fileService;
    private final RouteOrderingRepository routeOrderingRepository;
    private final RouteProductRepository routeProductRepository;
    private final CarTypeRepository carTypeRepository;
    private final AttachmentTagRepository attachmentTagRepository;
    private final ProjectAttachmentRepository projectAttachmentRepository;

    private final ProduceOrganizationClassification1Repository produceOrganizationClassification1Repository;
    private final ProduceOrganizationClassification2Repository produceOrganizationClassification2Repository;

    @Value("${default.image.address}")
    private String defaultImageAddress;

    private void uploadAttachments(List<ProjectAttachment> attachments, List<MultipartFile> filedAttachments) {
        // ?????? ????????? ????????? ????????? ?????? Multipart ?????????
        // ????????? ????????? unique name ??? ??????????????? ?????? ??????????????? ?????????
        IntStream.range(0, attachments.size())
                .forEach(
                        i -> fileService.upload
                                (
                                        filedAttachments.get(i),
                                        attachments.get(i).getUniqueName()
                                )
                );
    }

    @Transactional
    public IdResponse tempCreate(ProjectCreateRequest req) {

        Project project = projectRepository.save(
                ProjectCreateRequest.toTempEntity(
                        req,
                        projectTypeRepository,
                        memberRepository,
                        produceOrganizationRepository,
                        buyerOrganizationRepository,
                        carTypeRepository,
                        attachmentTagRepository
                )
        );
        if(!(req.getAttachments()== null || req.getAttachments().size()==0)) {
            uploadAttachments(project.getProjectAttachments(), req.getAttachments());
        }

        return new IdResponse(project.getId());
    }


    @Transactional
    public IdResponse create(ProjectCreateRequest req) {

        Project project = projectRepository.save(
                ProjectCreateRequest.toSaveEntity(
                        req,
                        projectTypeRepository,
                        memberRepository,
                        produceOrganizationRepository,
                        buyerOrganizationRepository,
                        carTypeRepository,
                        attachmentTagRepository
                )
        );

        if(!(req.getAttachments()==null || req.getAttachments().size()==0)) {
            uploadAttachments(project.getProjectAttachments(), req.getAttachments());
        }

        saveTrueAttachment(project);

        return new IdResponse(project.getId());
    }


    @Transactional
    public UpdateResponse update(Long id, ProjectUpdateRequest req) {

        Project project =  projectRepository.findById(id).orElseThrow(ProjectNotFoundException::new);

        List<Long> oldTags = produceOldNewTagComment(project, req).getOldTag();
        List<Long> newTags = produceOldNewTagComment(project, req).getNewTag();
        List<String> oldComment = produceOldNewTagComment(project, req).getOldComment();
        List<String> newComment =produceOldNewTagComment(project, req).getNewComment();
        List<ProjectAttachment> targetAttachmentsForTagAndComment
                = produceOldNewTagComment(project, req).getTargetAttachmentsForTagAndComment();
        Collections.sort(targetAttachmentsForTagAndComment, Comparator.comparing(EntityDate::getCreatedAt));

        Project.FileUpdatedResult result = project.update(
                req,
                projectTypeRepository,
                produceOrganizationRepository,
                carTypeRepository,
                memberRepository,
                attachmentTagRepository,
                oldTags,
                newTags,
                oldComment,
                newComment,
                targetAttachmentsForTagAndComment,
                "update"
        );

        uploadAttachments(
                result.getAttachmentUpdatedResult().getAddedAttachments(),
                result.getAttachmentUpdatedResult().getAddedAttachmentFiles()
        );

        deleteAttachments(
                result.getAttachmentUpdatedResult().getDeletedAttachments()
        );

        Long routeId = -1L;
        if(routeOrderingRepository.findByProjectOrderByIdAsc(project).size()>0) {
            RouteOrdering routeOrdering =
                    routeOrderingRepository.findByProjectOrderByIdAsc(project)
                            .get
                                    (
                                            routeOrderingRepository.findByProjectOrderByIdAsc(project).size()-1
                                    );
            routeId = routeOrdering.getId();
        }

        return new UpdateResponse(id, routeId);

    }

    @Transactional
    public UpdateResponse tempEnd(
            Long id, ProjectUpdateRequest req) {

        Project project = projectRepository.findById(id).
                orElseThrow(ProjectNotFoundException::new);

        List<Long> oldTags = produceOldNewTagComment(project, req).getOldTag();
        List<Long> newTags = produceOldNewTagComment(project, req).getNewTag();
        List<String> oldComment = produceOldNewTagComment(project, req).getOldComment();
        List<String> newComment =produceOldNewTagComment(project, req).getNewComment();
        List<ProjectAttachment> targetAttachmentsForTagAndComment
                = produceOldNewTagComment(project, req).getTargetAttachmentsForTagAndComment();
        Collections.sort(targetAttachmentsForTagAndComment, Comparator.comparing(EntityDate::getCreatedAt));

        Project.FileUpdatedResult result = project.update(
                req,
                projectTypeRepository,
                produceOrganizationRepository,
                carTypeRepository,
                memberRepository,
                attachmentTagRepository,

                oldTags,
                newTags,
                oldComment,
                newComment,

                targetAttachmentsForTagAndComment,

                "tempEnd"
        );

        uploadAttachments(
                result.getAttachmentUpdatedResult().getAddedAttachments(),
                result.getAttachmentUpdatedResult().getAddedAttachmentFiles()
        );
        deleteAttachments(
                result.getAttachmentUpdatedResult().getDeletedAttachments()
        );

        Long routeId = -1L;
        if(routeOrderingRepository.findByProjectOrderByIdAsc(project).size()>0) {
            RouteOrdering routeOrdering =
                    routeOrderingRepository.findByProjectOrderByIdAsc(project)
                            .get
                                    (
                                            routeOrderingRepository.findByProjectOrderByIdAsc(project).size()-1
                                    );
            routeId = routeOrdering.getId();
        }

        saveTrueAttachment(project);
        /////////////////////////////////////////////////////////////////////////

        return new UpdateResponse(id, routeId);

    }

    public Long routeIdReturn(Long id){

        Project project = projectRepository.findById(id).orElseThrow(ProjectNotFoundException::new);
        List<RouteOrdering> routeOrderings = routeOrderingRepository.findByProjectOrderByIdAsc(project);
        return routeOrderings.get(routeOrderings.size()-1).getId();
    }


    // read one project
    @Transactional
    public ProjectDto read(Long id){
        Project targetProject = projectRepository.findById(id).orElseThrow(ProjectNotFoundException::new);

        return ProjectDto.toDto(
                targetProject,
                routeOrderingRepository,
                routeProductRepository,
                attachmentTagRepository
        );
    }


//    public Page<ProjectDashboardDto> readPageAll(
//
//            Pageable pageRequest
//
//    ){
//
//        Page<Project> projectListBefore = projectRepository.findAll(pageRequest);//??????
//
//        List<Project> projectListList =
//                projectListBefore.stream().filter(
//                        i->
//                                i.getTempsave().equals(false)
//                                        && !i.isDeleted()
//                                        && !i.isPending()
//
//                ).collect(Collectors.toList());
//
//        System.out.println("project ??? ?????????");
//        for(Project p:projectListList){
//            System.out.println(p.getId() + " "+ p.isDeleted() );
//        }
//
//        Page<Project> workingProjectList = new PageImpl<>(projectListList);
//
//        Page<ProjectDashboardDto> workingPagingList = workingProjectList.map(
//                project -> new ProjectDashboardDto(
//                        project.getId(),
//                        project.getProjectNumber(),
//                        project.getName(),
//
//                        project.getProjectType().getName(),
//                        project.getProjectLevel().getName(),
//
//                        project.getProduceOrganization()==null?
//                                "":
//                                ProduceOrganizationDto.toDto(project.getProduceOrganization()).getName(),
//
//                        project.getClientOrganization()==null?
//                                "":
//                                ClientOrganizationDto.toDto(project.getClientOrganization()).getName(),
//
//                        project.getCarType()==null?
//                                CarTypeDto.toDto()
//                                : CarTypeDto.toDto(project.getCarType()),
//                        project.getClientItemNumber(),
//
//                        project.getNewItems()==null||project.getNewItems().size()==0?
//                                ItemProjectDashboardDto.toDtoList():
//                                ItemProjectDashboardDto.toDtoList(project.getNewItems(), routeOrderingRepository, routeProductRepository),
//
//                        project.getNewItems().size(), //0808 ?????? - ?????? ????????? ??????
//
//                        project.getTempsave(),
//
//                        project.getLifecycle(),
//
//                        routeOrderingRepository.findByProjectOrderByIdAsc(project).get(
//                                (
//                                        routeOrderingRepository.findByProjectOrderByIdAsc(project).size()-1
//                                )
//                        ).getLifecycleStatus(), // ???????????? ???????????? life cycle status
//
//                        project.getCreatedAt(),
//
//                        project.isDeleted(),
//                        project.isPending(),
//                        project.isDropped()
//
//                )
//        );
//        return workingPagingList;
//
//    }

    public Page<ProjectDto> readManagedProject(
            Pageable pageRequest,
            MemberIdReq req
    ){
        // project member ??? ?????? ?????? ????????????

        Member requestMember = memberRepository.findById(req.getMemberId()).orElseThrow(MemberNotFoundException::new);
        List<Project> projectMadeByMe = projectRepository.findByMember(requestMember).stream().filter(
                i->i.getTempsave().equals(false)
                        && (!i.isDeleted()||(i.isDeleted()&&i.getMember().getId()==req.getMemberId()))
        ).collect(Collectors.toList());

        Page<Project> workingProjectList = projectRepository.findByProjects(
                projectMadeByMe, pageRequest
        );

        return workingProjectList.map(
                d -> ProjectDto.toDto(d, routeOrderingRepository, routeProductRepository, attachmentTagRepository)
        );

    }

//    public Page<ProjectDashboardDto> readPageAllSearch(
//
//            Pageable pageRequest,
//            ProjectMemberRequest req,
//            String search
//
//    ){
//
//        List<Project> projectListBefore = projectRepository.findByNameContainingIgnoreCaseOrProjectNumberContainingIgnoreCaseOrLifecycleContainingIgnoreCaseOrClientItemNumberContainingIgnoreCase(
//                search,
//                search,
//                search,
//                search
//        );
//
//        List<Project> projectListList =
//                projectListBefore.stream().filter(
//                        i->i.getTempsave().equals(false)
//                                && (!i.isDeleted()||(i.isDeleted()&&i.getMember().getId()==req.getMemberId()))
//                ).collect(Collectors.toList());
//
//        List<Project> workingProjectListList =
//                projectListList.stream().filter(
//                        i->i.getLifecycle().equals("WORKING") //COMPLETE??? ????????? ??????
//                ).collect(Collectors.toList());
//
//        List<Project> completeProjectListList =
//                projectListList.stream().filter(
//                        i->i.getLifecycle().equals("COMPLETE") //COMPLETE??? ????????? ??????
//                ).collect(Collectors.toList());
//
//        Page<Project> workingProjectList = new PageImpl<>(projectListList);
//        //Page<Project> completeProjectList = new PageImpl<>(completeProjectListList);
//
//        Page<ProjectDashboardDto> workingPagingList = workingProjectList.map(
//                project -> new ProjectDashboardDto(
//                        project.getId(),
//                        project.getProjectNumber(),
//                        project.getName(),
//
//                        project.getProjectType().getName(),
//                        project.getProjectLevel().getName(),
//
//                        project.getProduceOrganization()==null?
//                                "":
//                                ProduceOrganizationDto.toDto(project.getProduceOrganization()).getName(),
//
//                        project.getClientOrganization()==null?
//                                "":
//                                ClientOrganizationDto.toDto(project.getClientOrganization()).getName(),
//
//                        project.getCarType()==null?
//                                CarTypeDto.toDto()
//                                : CarTypeDto.toDto(project.getCarType()),
//                        project.getClientItemNumber(),
//
//                        project.getNewItems()==null||project.getNewItems().size()==0?
//                                ItemProjectDashboardDto.toDtoList():
//                                ItemProjectDashboardDto.toDtoList(project.getNewItems(), routeOrderingRepository, routeProductRepository),
//
//                        project.getNewItems().size(), //0808 ?????? - ?????? ????????? ??????
//
//                        project.getTempsave(),
//
//                        project.getLifecycle(),
//
//                        routeOrderingRepository.findByProjectOrderByIdAsc(project).get(
//                                (
//                                        routeOrderingRepository.findByProjectOrderByIdAsc(project).size()-1
//                                )
//                        ).getLifecycleStatus(), // ???????????? ???????????? life cycle status
//
//                        project.getCreatedAt(),
//
//                        project.isDeleted(),
//                        project.isPending(),
//                        project.isDropped()
//
//                )
//        );
//
//
//        return workingPagingList;
//
//    }
//
//    public Page<ProjectDashboardDto> readDashboard(
//
//            Pageable pageRequest,
//            ProjectMemberRequest req
//
//    ){
//
//        // ?????? ????????? ??????????????? ???
//        List<Project> myProjectList = projectRepository.
//                findByMember(
//                        memberRepository.findById(req.getMemberId())
//                                .orElseThrow(MemberNotFoundException::new)
//                );
//
//        List<Project> projects = new ArrayList<>();
//
//        // ???????????? ?????? ???????????? ??????????????????!
//        for(Project project : myProjectList){
//            if(!project.getTempsave()) { //??????????????? ????????? ?????????
//                projects.add(project);
//            }
//        }
//
//        Page<Project> projectList = projectRepository.findByProjects(projects, pageRequest);
//
//        return projectList.map(
//                d -> ProjectDashboardDto.toDto(d, routeOrderingRepository, routeProductRepository)
//        );
//
//    }


    public List<Project> searchProjectWithKeywords(@Nullable String search) {

        List<Project> totalList = new ArrayList<>();

        if(search!=null&&search.length()>0&&!search.contains("\n")){
            totalList  = projectRepository.findByNameContainingIgnoreCaseOrProjectNumberContainingIgnoreCaseOrLifecycleContainingIgnoreCase(
                    search,
                    search,
                    search
            );

        }else {
            totalList = projectRepository.findAll();
        }

        return totalList.stream().filter(

                i->
                        i.getTempsave().equals(false)

        ).collect(Collectors.toList());
    }

    public Page<ProjectDto> returnPagingProject(
            String name,
            Pageable pageRequest

    ){

        Page<Project> projectList = projectRepository.findByProjects(searchProjectWithKeywords(name), pageRequest);

        return projectList.map(
                d -> ProjectDto.toDto(
                        d,
                        routeOrderingRepository,
                        routeProductRepository,
                        attachmentTagRepository
                        )
        );
    }


    @Transactional
    public void delete(Long id) {
        Project project = projectRepository.findById(id).orElseThrow(ProjectNotFoundException::new);
        deleteProjectAttachments(project.getProjectAttachments());
        projectRepository.delete(project);
    }



    private void deleteProjectAttachments(List<ProjectAttachment> projectAttachments) {
        projectAttachments.forEach(i -> fileService.delete(i.getUniqueName()));
    }

    private void saveTrueAttachment(Project target) {
        projectAttachmentRepository.findByProject(target).
                forEach(
                        i->i.setSave(true)
                );
    }

//    private void uploadAttachments(List<ProjectAttachment> attachments, List<MultipartFile> filedAttachments) {
//        // ?????? ????????? ????????? ????????? ?????? Multipart ?????????
//        // ????????? ????????? unique name ??? ??????????????? ?????? ??????????????? ?????????
//        IntStream.range(0, attachments.size())
//                .forEach(
//                        i -> fileService.upload
//                                (
//                                        filedAttachments.get(i),
//                                        attachments.get(i).getUniqueName()
//                                )
//                );
//    }

    private void deleteAttachments(List<ProjectAttachment> attachments) {
        for(ProjectAttachment attachment:attachments){
            if(!attachment.isSave()){
                fileService.delete(attachment.getUniqueName());
            }
        }
    }

    @Getter
    @AllArgsConstructor
    public static class OldNewTagCommentUpdatedResult {
        private List<Long> oldTag;
        private List<Long> newTag;
        private List<String> oldComment;
        private List<String> newComment;

        private List<ProjectAttachment> targetAttachmentsForTagAndComment;
    }

    public ProjectService.OldNewTagCommentUpdatedResult produceOldNewTagComment(
            Project entity, //update ????????? ?????????
            ProjectUpdateRequest req
    ) {
        List<Long> oldDocTag = new ArrayList<>();
        List<Long> newDocTag = new ArrayList<>();
        List<String> oldDocComment = new ArrayList<>();
        List<String> newDocComment = new ArrayList<>();

        List<ProjectAttachment> newItemAttachments
                = entity.getProjectAttachments();

        List<ProjectAttachment> targetAttachmentsForTagAndComment = new ArrayList<>();

        // OLD TAG, NEW TAG, COMMENT OLD NEW GENERATE

        // ?????? ??????, ????????? ?????? ????????? ????????? ?????? ???
        if (newItemAttachments.size() > 0 && req.getAddedAttachments().size()>0 ) {
            // ?????? ?????? ?????? ??????(?????? ??????, ????????? ????????? ???) == ?????? deleted ????????? - deleted=true ??? ??????

            // ????????? ??? ?????????
            List<ProjectAttachment> oldAttachments = entity.getProjectAttachments();
            for (ProjectAttachment attachment : oldAttachments) {

                if (
                        (!attachment.isDeleted())
                    // (1) ????????? ?????? : DELETED = FALSE (??????, ????????? ?????? ???????????? ?????? ??????????????? ??????)
                ) {
                    if(req.getDeletedAttachments() != null){ // (1-1) ?????? ???????????? delete ??? ???????????? ???????????????
                        if(!(req.getDeletedAttachments().contains(attachment.getId()))){
                            // ??? delete ?????? ??? attachment ????????? ???????????? ?????? ?????? ??????
                            targetAttachmentsForTagAndComment.add(attachment);
                        }
                    }
                    else{ //??? ????????? delete ????????? ????????? ??? ???????????? ??????
                        targetAttachmentsForTagAndComment.add(attachment);
                    }

                }

            }

            int standardIdx = targetAttachmentsForTagAndComment.size();

            oldDocTag.addAll(req.getAddedTag().subList(0, standardIdx));
            newDocTag.addAll(req.getAddedTag().subList(standardIdx, req.getAddedTag().size()));

            int idx=0;
            if (req.getAddedTag().size()>0 && req.getAddedAttachmentComment().size()==0) {
                List<String> refineString = new ArrayList<>();
                while(idx < req.getAddedTag().size()){
                    refineString.add(" ");
                    idx+=1;
                }

                oldDocComment.addAll(refineString.subList(0, standardIdx));
                newDocComment.addAll(refineString.subList(standardIdx, refineString.size()));

            }
            else {
                oldDocComment.addAll(req.getAddedAttachmentComment().subList(0, standardIdx));
                newDocComment.addAll(req.getAddedAttachmentComment().subList(standardIdx, req.getAddedAttachmentComment().size()));
            }
        }
        // ?????? ????????? ????????? ?????? ???
        else if(newItemAttachments.size()>0){
            System.out.println(" ?????? ???????????? ?????? ?????? ???????????? ?????? go ");

            // ????????? ??? ?????????
            List<ProjectAttachment> oldAttachments = entity.getProjectAttachments();
            for (ProjectAttachment attachment : oldAttachments) {

                if (
                        (!attachment.isDeleted())
                    // (1) ????????? ?????? : DELETED = FALSE (??????, ????????? ?????? ???????????? ?????? ??????????????? ??????)
                ) {
                    if(req.getDeletedAttachments() != null){ // (1-1) ?????? ???????????? delete ??? ???????????? ???????????????
                        if(!(req.getDeletedAttachments().contains(attachment.getId()))){
                            // ??? delete ?????? ??? attachment ????????? ???????????? ?????? ?????? ??????
                            targetAttachmentsForTagAndComment.add(attachment);
                        }
                    }
                    else{ //??? ????????? delete ????????? ????????? ??? ???????????? ??????
                        targetAttachmentsForTagAndComment.add(attachment);
                    }

                }

            }

            oldDocTag.addAll(req.getAddedTag());
            oldDocComment.addAll(req.getAddedAttachmentComment());

        }
        //????????? ????????? ?????????, ????????? ?????? ?????? ???
        else{

            // ????????? ??? ?????????
            List<ProjectAttachment> oldAttachments = entity.getProjectAttachments();
            for (ProjectAttachment attachment : oldAttachments) {

                if (
                        (!attachment.isDeleted())
                    // (1) ????????? ?????? : DELETED = FALSE (??????, ????????? ?????? ???????????? ?????? ??????????????? ??????)
                ) {
                    if(req.getDeletedAttachments() != null){ // (1-1) ?????? ???????????? delete ??? ???????????? ???????????????
                        if(!(req.getDeletedAttachments().contains(attachment.getId()))){
                            // ??? delete ?????? ??? attachment ????????? ???????????? ?????? ?????? ??????
                            targetAttachmentsForTagAndComment.add(attachment);
                        }
                    }
                    else{ //??? ????????? delete ????????? ????????? ??? ???????????? ??????
                        targetAttachmentsForTagAndComment.add(attachment);
                    }

                }

            }

            System.out.println(" ????????? ????????? ?????????, ????????? ?????? ?????? ??? ");
            newDocTag.addAll(req.getAddedTag().subList(0,req.getAddedTag().size()));
            newDocComment.addAll(req.getAddedAttachmentComment().subList(0, req.getAddedAttachmentComment().size()));

        }
        return new ProjectService.OldNewTagCommentUpdatedResult(

                oldDocTag,
                newDocTag,
                oldDocComment,
                newDocComment,

                targetAttachmentsForTagAndComment
        );
    }

    @Transactional
    public Long deleted(Long id) {
        Project project = projectRepository.findById(id).orElseThrow(ProjectNotFoundException::new);
        project.updateDeleted();
        return id;
    }

    @Transactional
    public Long drop(Long id) {
        Project project = projectRepository.findById(id).orElseThrow(ProjectNotFoundException::new);
        project.updateDrop();
        return id;
    }

    @Transactional
    public Long pending(Long id) {
        Project project = projectRepository.findById(id).orElseThrow(ProjectNotFoundException::new);
        project.updatePending();
        return id;
    }
}


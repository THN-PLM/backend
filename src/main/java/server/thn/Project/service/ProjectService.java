package server.thn.Project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import server.thn.Common.dto.response.IdResponse;
import server.thn.File.repository.AttachmentTagRepository;
import server.thn.File.service.FileService;
import server.thn.Member.repository.MemberRepository;
import server.thn.Project.dto.ProjectCreateRequest;
import server.thn.Project.entity.Project;
import server.thn.Project.entity.ProjectAttachment;
import server.thn.Project.repository.ProjectAttachmentRepository;
import server.thn.Project.repository.ProjectRepository;
import server.thn.Project.repository.ProjectTypeRepository;
import server.thn.Project.repository.buyer.BuyerOrganizationRepository;
import server.thn.Project.repository.carType.CarTypeRepository;
import server.thn.Project.repository.produceOrg.ProduceOrganizationRepository;
import server.thn.Route.repository.RouteOrderingRepository;
import server.thn.Route.repository.RouteProductRepository;

import java.util.List;
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

    @Value("${default.image.address}")
    private String defaultImageAddress;

    private void uploadAttachments(List<ProjectAttachment> attachments, List<MultipartFile> filedAttachments) {
        // 실제 이미지 파일을 가지고 있는 Multipart 파일을
        // 파일이 가지는 unique name 을 파일명으로 해서 파일저장소 업로드
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
        if(!(req.getAttachments()==null || req.getAttachments().size()==0)) {
            uploadAttachments(project.getProjectAttachments(), req.getAttachments());
        }

        return new IdResponse(project.getId());
    }

    private void saveTrueAttachment(Project target) {
        projectAttachmentRepository.findByProject(target).
                forEach(
                        i->i.setSave(true)
                );

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

//
//    @Transactional
//
//    public ItemUpdateResponse update(Long id, ProjectUpdateRequest req) {
//
//        Project project =  projectRepository.findById(id).orElseThrow(ProjectNotFoundException::new);
//
//        List<Long> oldTags = produceOldNewTagComment(project, req).getOldTag();
//        List<Long> newTags = produceOldNewTagComment(project, req).getNewTag();
//        List<String> oldComment = produceOldNewTagComment(project, req).getOldComment();
//        List<String> newComment =produceOldNewTagComment(project, req).getNewComment();
//        List<ProjectAttachment> targetAttachmentsForTagAndComment
//                = produceOldNewTagComment(project, req).getTargetAttachmentsForTagAndComment();
//        Collections.sort(targetAttachmentsForTagAndComment, Comparator.comparing(EntityDate::getCreatedAt));
//
//        Project.FileUpdatedResult result = project.update(
//                req,
//                newItemRepository,
//                projectTypeRepository,
//                projectLevelRepository,
//                produceOrganizationRepository,
//                clientOrganizationRepository,
//                carTypeRepository,
//                memberRepository,
//                attachmentTagRepository,
//
//                oldTags,
//                newTags,
//                oldComment,
//                newComment,
//
//                targetAttachmentsForTagAndComment
//
//        );
//
//
//        uploadAttachments(
//                result.getAttachmentUpdatedResult().getAddedAttachments(),
//                result.getAttachmentUpdatedResult().getAddedAttachmentFiles()
//        );
//        deleteAttachments(
//                result.getAttachmentUpdatedResult().getDeletedAttachments()
//        );
//
//        Long routeId = -1L;
//        if(routeOrderingRepository.findByProjectOrderByIdAsc(project).size()>0) {
//            RouteOrdering routeOrdering =
//                    routeOrderingRepository.findByProjectOrderByIdAsc(project)
//                            .get
//                                    (
//                                            routeOrderingRepository.findByProjectOrderByIdAsc(project).size()-1
//                                    );
//            routeId = routeOrdering.getId();
//        }
//
//        return new ItemUpdateResponse(id, routeId);
//
//    }
//
//    @Transactional
//    public ItemUpdateResponse tempEnd(
//            Long id, ProjectUpdateRequest req) {
//
//        Project project = projectRepository.findById(id).
//                orElseThrow(ProjectNotFoundException::new);
//
//        List<Long> oldTags = produceOldNewTagComment(project, req).getOldTag();
//        List<Long> newTags = produceOldNewTagComment(project, req).getNewTag();
//        List<String> oldComment = produceOldNewTagComment(project, req).getOldComment();
//        List<String> newComment =produceOldNewTagComment(project, req).getNewComment();
//        List<ProjectAttachment> targetAttachmentsForTagAndComment
//                = produceOldNewTagComment(project, req).getTargetAttachmentsForTagAndComment();
//        Collections.sort(targetAttachmentsForTagAndComment, Comparator.comparing(EntityDate::getCreatedAt));
//
//        Project.FileUpdatedResult result = project.tempEnd(
//                req,
//                newItemRepository,
//                projectTypeRepository,
//                projectLevelRepository,
//                produceOrganizationRepository,
//                clientOrganizationRepository,
//                carTypeRepository,
//                memberRepository,
//                attachmentTagRepository,
//
//                oldTags,
//                newTags,
//                oldComment,
//                newComment,
//
//                targetAttachmentsForTagAndComment
//        );
//
//        uploadAttachments(
//                result.getAttachmentUpdatedResult().getAddedAttachments(),
//                result.getAttachmentUpdatedResult().getAddedAttachmentFiles()
//        );
//        deleteAttachments(
//                result.getAttachmentUpdatedResult().getDeletedAttachments()
//        );
//
//        Long routeId = -1L;
//        if(routeOrderingRepository.findByProjectOrderByIdAsc(project).size()>0) {
//            RouteOrdering routeOrdering =
//                    routeOrderingRepository.findByProjectOrderByIdAsc(project)
//                            .get
//                                    (
//                                            routeOrderingRepository.findByProjectOrderByIdAsc(project).size()-1
//                                    );
//            routeId = routeOrdering.getId();
//        }
//
//        saveTrueAttachment(project);
//        /////////////////////////////////////////////////////////////////////////
//
//        return new ItemUpdateResponse(id, routeId);
//
//    }
//
//    public Long routeIdReturn(Long newCreateItemId){
//
//        NewItem newItemOfProject = newItemRepository.findById(newCreateItemId).orElseThrow(ItemNotFoundException::new);
//
//        List<RouteOrdering> routeOrdering = routeOrderingRepository.findByNewItemOrderByIdAsc(newItemOfProject);
//
//        //프로젝트에 딸린 라우트
//        Long routeId = routeOrderingRepository.findByNewItemOrderByIdAsc(newItemOfProject).get(routeOrdering.size()-1).getId();
//
//        return routeId;
//    }
//
//
//    // read one project
//    @Transactional
//    public ProjectDto read(Long id){
//        Project targetProject = projectRepository.findById(id).orElseThrow(ProjectNotFoundException::new);
//
//        return ProjectDto.toDto(
//                targetProject,
//                routeOrderingRepository,
//                routeProductRepository,
//                bomRepository,
//                preliminaryBomRepository,
//                attachmentTagRepository,
//                defaultImageAddress
//        );
//
//    }
//
//    public Page<ProjectDashboardDto> readPageAll(
//
//            Pageable pageRequest
//
//    ){
//
//        Page<Project> projectListBefore = projectRepository.findAll(pageRequest);//에러
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
//        System.out.println("project 좀 봐봐라");
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
//                        project.getNewItems().size(), //0808 추가 - 엮인 아이템 갯수
//
//                        project.getTempsave(),
//
//                        project.getLifecycle(),
//
//                        routeOrderingRepository.findByProjectOrderByIdAsc(project).get(
//                                (
//                                        routeOrderingRepository.findByProjectOrderByIdAsc(project).size()-1
//                                )
//                        ).getLifecycleStatus(), // 프로젝트 라우트의 life cycle status
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
//
//    public Page<ProjectDashboardDto> readManagedProject(
//            Pageable pageRequest,
//            ProjectMemberRequest req
//    ){
//        // project member 가 나인 것만 데려오기
//
//        Member requestMember = memberRepository.findById(req.getMemberId()).orElseThrow(MemberNotFoundException::new);
//        List<Project> projectMadeByMe = projectRepository.findByMember(requestMember).stream().filter(
//                i->i.getTempsave().equals(false)
//                        && (!i.isDeleted()||(i.isDeleted()&&i.getMember().getId()==req.getMemberId()))
//        ).collect(Collectors.toList());
//
//        Page<Project> workingProjectList = projectRepository.findByProjects(
//                projectMadeByMe, pageRequest
//        );
//
//        return workingProjectList.map(
//                d -> ProjectDashboardDto.toDto(d, routeOrderingRepository, routeProductRepository)
//        );
//
//    }
//
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
//                        i->i.getLifecycle().equals("WORKING") //COMPLETE인 애들은 따로
//                ).collect(Collectors.toList());
//
//        List<Project> completeProjectListList =
//                projectListList.stream().filter(
//                        i->i.getLifecycle().equals("COMPLETE") //COMPLETE인 애들은 따로
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
//                        project.getNewItems().size(), //0808 추가 - 엮인 아이템 갯수
//
//                        project.getTempsave(),
//
//                        project.getLifecycle(),
//
//                        routeOrderingRepository.findByProjectOrderByIdAsc(project).get(
//                                (
//                                        routeOrderingRepository.findByProjectOrderByIdAsc(project).size()-1
//                                )
//                        ).getLifecycleStatus(), // 프로젝트 라우트의 life cycle status
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
//        // 내가 작성한 프로젝트들 중
//        List<Project> myProjectList = projectRepository.
//                findByMember(
//                        memberRepository.findById(req.getMemberId())
//                                .orElseThrow(MemberNotFoundException::new)
//                );
//
//        List<Project> projects = new ArrayList<>();
//
//        // 라우트가 이미 만들어진 프로젝트들만!
//        for(Project project : myProjectList){
//            if(!project.getTempsave()) { //임시저장이 거짓인 애들만
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
//
//    // 수정된 project search
//    public List<Project> searchProjectWithKeywords(@Nullable String search) {
//
//        List<Project> totalList = new ArrayList<>();
//
//        if(search!=null&&search.length()>0&&!search.contains("\n")){
//            totalList  = projectRepository.findByNameContainingIgnoreCaseOrProjectNumberContainingIgnoreCaseOrLifecycleContainingIgnoreCaseOrClientItemNumberContainingIgnoreCase(
//                    search,
//                    search,
//                    search,
//                    search
//            );
//
//        }else {
//            totalList = projectRepository.findAll();
//        }
//
//        return totalList.stream().filter(
//
//                i->
//                        i.getTempsave().equals(false)
////                            &&
////                                    (
////                                                i.getStatus().equals("cad")
////                                            ||
////                                                i.getStatus().equals("cadian")
////                                    )
//
//        ).collect(Collectors.toList());
//    }
//
//    public Page<ProjectDashboardDto> returnPagingProject(
//            String name,
//            Pageable pageRequest
//
//    ){
//        Page<Project> projectList = projectRepository.findByProjects(searchProjectWithKeywords(name), pageRequest);
//
//        return projectList.map(
//                d -> ProjectDashboardDto.toDto(d, routeOrderingRepository, routeProductRepository)
//        );
//    }
//
//    //delete one project
//
//    @Transactional
//    public void delete(Long id) {
//        Project project = projectRepository.findById(id).orElseThrow(ProjectNotFoundException::new);
//        deleteProjectAttachments(project.getProjectAttachments());
//        projectRepository.delete(project);
//    }
//
//    public NewItemCreateResponse projectUpdateToReadonlyFalseTempsaveTrue(Project project){
//
//        project.projectUpdateToReadonlyFalseTempSaveTrue();
//        return new NewItemCreateResponse(project.getId());
//    }
//
//
//    private void deleteProjectAttachments(List<ProjectAttachment> projectAttachments) {
//        projectAttachments.forEach(i -> fileService.delete(i.getUniqueName()));
//    }
//
//    private void saveTrueAttachment(Project target) {
//        projectAttachmentRepository.findByProject(target).
//                forEach(
//                        i->i.setSave(true)
//                );
//    }
//
//    private void uploadAttachments(List<ProjectAttachment> attachments, List<MultipartFile> filedAttachments) {
//        // 실제 이미지 파일을 가지고 있는 Multipart 파일을
//        // 파일이 가지는 unique name 을 파일명으로 해서 파일저장소 업로드
//        IntStream.range(0, attachments.size())
//                .forEach(
//                        i -> fileService.upload
//                                (
//                                        filedAttachments.get(i),
//                                        attachments.get(i).getUniqueName()
//                                )
//                );
//    }
//
//    private void deleteAttachments(List<ProjectAttachment> attachments) {
//        for(ProjectAttachment attachment:attachments){
//            if(!attachment.isSave()){
//                fileService.delete(attachment.getUniqueName());
//            }
//        }
//    }
//
//    @Getter
//    @AllArgsConstructor
//    public static class OldNewTagCommentUpdatedResult {
//        private List<Long> oldTag;
//        private List<Long> newTag;
//        private List<String> oldComment;
//        private List<String> newComment;
//
//        private List<ProjectAttachment> targetAttachmentsForTagAndComment;
//    }
//
//    public ProjectService.OldNewTagCommentUpdatedResult produceOldNewTagComment(
//            Project entity, //update 당하는 아이템
//            ProjectUpdateRequest req
//    ) {
//        List<Long> oldDocTag = new ArrayList<>();
//        List<Long> newDocTag = new ArrayList<>();
//        List<String> oldDocComment = new ArrayList<>();
//        List<String> newDocComment = new ArrayList<>();
//
//        List<ProjectAttachment> newItemAttachments
//                = entity.getProjectAttachments();
//
//        List<ProjectAttachment> targetAttachmentsForTagAndComment = new ArrayList<>();
//
//        // OLD TAG, NEW TAG, COMMENT OLD NEW GENERATE
//
//        // 예전 태그, 코멘트 있고 추가된 문서도 있을 때
//        if (newItemAttachments.size() > 0 && req.getAddedAttachments().size()>0 ) {
//            // 올드 문서 기존 갯수(올드 태그, 코멘트 적용할 것) == 빼기 deleted 아이디 - deleted=true 인 애들
//
//            // 일단은 다 가져와
//            List<ProjectAttachment> oldAttachments = entity.getProjectAttachments();
//            for (ProjectAttachment attachment : oldAttachments) {
//
//                if (
//                        (!attachment.isDeleted())
//                    // (1) 첫번째 조건 : DELETED = FALSE (태그, 코멘트 적용 대상들은 현재 살아있어야 하고)
//                ) {
//                    if(req.getDeletedAttachments() != null){ // (1-1) 만약 사용자가 delete 를 입력한게 존재한다면
//                        if(!(req.getDeletedAttachments().contains(attachment.getId()))){
//                            // 그 delete 안에 이 attachment 아이디 존재하지 않을 때만 추가
//                            targetAttachmentsForTagAndComment.add(attachment);
//                        }
//                    }
//                    else{ //걍 애초에 delete 하는거 없으면 걍 더해주면 되지
//                        targetAttachmentsForTagAndComment.add(attachment);
//                    }
//
//                }
//
//            }
//
//            int standardIdx = targetAttachmentsForTagAndComment.size();
//
//            oldDocTag.addAll(req.getAddedTag().subList(0, standardIdx));
//            newDocTag.addAll(req.getAddedTag().subList(standardIdx, req.getAddedTag().size()));
//
//            int idx=0;
//            if (req.getAddedTag().size()>0 && req.getAddedAttachmentComment().size()==0) {
//                List<String> refineString = new ArrayList<>();
//                while(idx < req.getAddedTag().size()){
//                    refineString.add(" ");
//                    idx+=1;
//                }
//
//                oldDocComment.addAll(refineString.subList(0, standardIdx));
//                newDocComment.addAll(refineString.subList(standardIdx, refineString.size()));
//
//            }
//            else {
//                oldDocComment.addAll(req.getAddedAttachmentComment().subList(0, standardIdx));
//                newDocComment.addAll(req.getAddedAttachmentComment().subList(standardIdx, req.getAddedAttachmentComment().size()));
//            }
//        }
//        // 새로 추가된 아이는 없을 때
//        else if(newItemAttachments.size()>0){
//            System.out.println(" 새로 추가된앤 없고 기존 애들거만 갱신 go ");
//
//            // 일단은 다 가져와
//            List<ProjectAttachment> oldAttachments = entity.getProjectAttachments();
//            for (ProjectAttachment attachment : oldAttachments) {
//
//                if (
//                        (!attachment.isDeleted())
//                    // (1) 첫번째 조건 : DELETED = FALSE (태그, 코멘트 적용 대상들은 현재 살아있어야 하고)
//                ) {
//                    if(req.getDeletedAttachments() != null){ // (1-1) 만약 사용자가 delete 를 입력한게 존재한다면
//                        if(!(req.getDeletedAttachments().contains(attachment.getId()))){
//                            // 그 delete 안에 이 attachment 아이디 존재하지 않을 때만 추가
//                            targetAttachmentsForTagAndComment.add(attachment);
//                        }
//                    }
//                    else{ //걍 애초에 delete 하는거 없으면 걍 더해주면 되지
//                        targetAttachmentsForTagAndComment.add(attachment);
//                    }
//
//                }
//
//            }
//
//            oldDocTag.addAll(req.getAddedTag());
//            oldDocComment.addAll(req.getAddedAttachmentComment());
//
//        }
//        //기존에 암것도 없었고, 추가된 것만 있을 때
//        else{
//
//            // 일단은 다 가져와
//            List<ProjectAttachment> oldAttachments = entity.getProjectAttachments();
//            for (ProjectAttachment attachment : oldAttachments) {
//
//                if (
//                        (!attachment.isDeleted())
//                    // (1) 첫번째 조건 : DELETED = FALSE (태그, 코멘트 적용 대상들은 현재 살아있어야 하고)
//                ) {
//                    if(req.getDeletedAttachments() != null){ // (1-1) 만약 사용자가 delete 를 입력한게 존재한다면
//                        if(!(req.getDeletedAttachments().contains(attachment.getId()))){
//                            // 그 delete 안에 이 attachment 아이디 존재하지 않을 때만 추가
//                            targetAttachmentsForTagAndComment.add(attachment);
//                        }
//                    }
//                    else{ //걍 애초에 delete 하는거 없으면 걍 더해주면 되지
//                        targetAttachmentsForTagAndComment.add(attachment);
//                    }
//
//                }
//
//            }
//
//            System.out.println(" 기존에 암것도 없었고, 추가된 것만 있을 때 ");
//            newDocTag.addAll(req.getAddedTag().subList(0,req.getAddedTag().size()));
//            newDocComment.addAll(req.getAddedAttachmentComment().subList(0, req.getAddedAttachmentComment().size()));
//
//        }
//        return new ProjectService.OldNewTagCommentUpdatedResult(
//
//                oldDocTag,
//                newDocTag,
//                oldDocComment,
//                newDocComment,
//
//                targetAttachmentsForTagAndComment
//        );
//    }
//
//    @Transactional
//    public Long deleted(Long id) {
//        Project project = projectRepository.findById(id).orElseThrow(ProjectNotFoundException::new);
//        project.updateDeleted();
//        return id;
//    }
//
//    @Transactional
//    public Long drop(Long id) {
//        Project project = projectRepository.findById(id).orElseThrow(ProjectNotFoundException::new);
//        project.updateDrop();
//        return id;
//    }
//
//    @Transactional
//    public Long pending(Long id) {
//        Project project = projectRepository.findById(id).orElseThrow(ProjectNotFoundException::new);
//        project.updatePending();
//        return id;
//    }
}


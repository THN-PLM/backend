package server.thn.Project.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.web.multipart.MultipartFile;
import server.thn.Common.entity.EntityDate;
import server.thn.File.exception.AttachmentNotFoundException;
import server.thn.File.exception.AttachmentTagNotFoundException;
import server.thn.File.repository.AttachmentTagRepository;
import server.thn.Member.entity.Member;
import server.thn.Member.exception.MemberNotFoundException;
import server.thn.Member.repository.MemberRepository;
import server.thn.Project.dto.ProjectUpdateRequest;
import server.thn.Project.exception.CarTypeNotFoundException;
import server.thn.Project.exception.ProduceOrganizationNotFoundException;
import server.thn.Project.exception.ProjectTypeNotFoundException;
import server.thn.Project.repository.ProjectTypeRepository;
import server.thn.Project.repository.carType.CarTypeRepository;
import server.thn.Project.repository.produceOrg.ProduceOrganizationRepository;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Project extends EntityDate {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE2")
    @SequenceGenerator(name="SEQUENCE2", sequenceName="SEQUENCE2", allocationSize=1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_type_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ProjectType projectType;

    @Column(nullable = false)
    private String name;

    @Column
    private String projectNumber;

    @Column
    private LocalDate allDoStartPeriod;

    @Column
    private LocalDate allDoOverPeriod;

    @Column
    private LocalDate protoStartPeriod;

    @Column
    private LocalDate protoOverPeriod;

    @Column
    private LocalDate p1StartPeriod;

    @Column
    private LocalDate p1OverPeriod;

    @Column
    private LocalDate p2StartPeriod;

    @Column
    private LocalDate p2OverPeriod;

    @Column
    private LocalDate mStartPeriod;

    @Column
    private LocalDate mOverPeriod;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "member_id",
            nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "modifier_id",
            nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member modifier;

    @Column(nullable = false)
    private Boolean tempsave;

    @Column(nullable = false)
    private Boolean readonly;

    @Column(nullable = false)
    private String lifecycle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produceOrganization_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ProduceOrganization produceOrganization;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyerOrganization_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private BuyerOrganization buyerOrganization;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carType_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private CarType carType;


    @OneToMany(
            mappedBy = "project",
            cascade = CascadeType.PERSIST,
            orphanRemoval = true
    )
    private List<ProjectAttachment> projectAttachments;

    @Column
    private char revision;

    // ??????
    @Column
    private boolean deleted;

    // pending
    @Column
    private boolean pending;

    // drop
    @Column
    private boolean dropped;


//    @OneToMany(
//            mappedBy = "project",
//            cascade = CascadeType.PERSIST,
//            orphanRemoval = true
//    )
//    private List<Product> products = new ArrayList<>();

    public Project(
            ProjectType projectType,
            String name,
            String projectNumber,

            LocalDate allDoStartPeriod,
            LocalDate allDoOverPeriod,

            LocalDate protoStartPeriod,
            LocalDate protoOverPeriod,

            LocalDate p1StartPeriod,
            LocalDate p1OverPeriod,

            LocalDate p2StartPeriod,
            LocalDate p2OverPeriod,

            LocalDate mStartPeriod,
            LocalDate mOverPeriod,

//            List<Product> product,

            Member member,

            Boolean tempsave,
            Boolean readonly,


            ProduceOrganization produceOrganization,

            BuyerOrganization buyerOrganization,
            List<ProjectAttachment> projectAttachments,

            CarType carType

    ) {
        this.name = name;
        this.projectType = projectType;
        this.projectNumber = projectNumber;

        this.member = member;
        this.modifier = member;

        this.tempsave = tempsave;
        this.readonly = readonly;

        this.allDoStartPeriod = allDoStartPeriod;
        this.allDoOverPeriod = allDoOverPeriod;

        this.protoStartPeriod = protoStartPeriod;
        this.protoOverPeriod = protoOverPeriod;

        this.p1StartPeriod = p1StartPeriod;
        this.p1OverPeriod = p1OverPeriod;

        this.p2StartPeriod = p2StartPeriod;
        this.p2OverPeriod = p2OverPeriod;

        this.mStartPeriod = mStartPeriod;
        this.mOverPeriod = mOverPeriod;

        if(produceOrganization!=null){
            this.produceOrganization = produceOrganization;
        }

        if(buyerOrganization!=null) {
            this.buyerOrganization = buyerOrganization;
        }

//        if(item!=null&&item.size()>0) {
//            addNewItems(item);
//        }

        if(carType!=null) {
            this.carType = carType;
        }

        this.projectAttachments = new ArrayList<>();
        addProjectAttachments(projectAttachments);

        this.revision = (char) 65;
        this.lifecycle = "WORKING";

        this.modifier = member;
    }

    // 11/28 update ??????


    public FileUpdatedResult update(
            ProjectUpdateRequest req,

            ProjectTypeRepository projectTypeRepository,

            ProduceOrganizationRepository produceOrganizationRepository,

            CarTypeRepository carTypeRepository,
            MemberRepository memberRepository,
            AttachmentTagRepository attachmentTagRepository,

            List<Long> oldTags,
            List<Long> newTags,

            List<String> oldComment,
            List<String> newComment,

            List<ProjectAttachment> targetAttaches,

            String tempEndOrUpdate
    ) {

        this.readonly = false;
        this.tempsave = true;

        this.setModifiedAt(LocalDateTime.now());

        this.name = req.getName().isBlank() ? this.name : req.getName();

        this.projectType =
                req.getProjectTypeId() == null ?
                        this.projectType :
                        projectTypeRepository.findById(req.getProjectTypeId())
                                .orElseThrow(ProjectTypeNotFoundException::new);

        this.protoStartPeriod =
                req.getProtoStartPeriod() == null ||
                        req.getProtoStartPeriod().isBlank() ?
                        this.protoStartPeriod :
                        LocalDate.parse(req.getProtoStartPeriod(), DateTimeFormatter.ISO_DATE);

        this.protoOverPeriod =
                req.getProtoStartPeriod() == null ||
                        req.getProtoOverPeriod().isBlank() ?
                        this.protoOverPeriod :
                        LocalDate.parse(req.getProtoOverPeriod(), DateTimeFormatter.ISO_DATE);

        this.p1StartPeriod =
                req.getP1StartPeriod() == null ||
                        req.getP1StartPeriod().isBlank() ?
                        this.p1StartPeriod :
                        LocalDate.parse(req.getP1StartPeriod(), DateTimeFormatter.ISO_DATE);

        this.p1OverPeriod =
                req.getP1OverPeriod() == null ||
                        req.getP1OverPeriod().isBlank() ?
                        this.p1OverPeriod :
                        LocalDate.parse(req.getP1OverPeriod(), DateTimeFormatter.ISO_DATE);

        this.p2StartPeriod =
                req.getP2StartPeriod() == null ||
                        req.getP2StartPeriod().isBlank() ?
                        this.p2StartPeriod :
                        LocalDate.parse(req.getP2StartPeriod(), DateTimeFormatter.ISO_DATE);

        this.p2OverPeriod =
                req.getP2OverPeriod() == null ||
                        req.getP2OverPeriod().isBlank() ?
                        this.protoOverPeriod :
                        LocalDate.parse(req.getP2OverPeriod(), DateTimeFormatter.ISO_DATE);

        this.mStartPeriod=
                req.getMStartPeriod() == null ||
                        req.getMStartPeriod().isBlank() ?
                        this.mStartPeriod :
                        LocalDate.parse(req.getMStartPeriod(), DateTimeFormatter.ISO_DATE);

        this.mOverPeriod =
                req.getMOverPeriod() == null ||
                        req.getMOverPeriod().isBlank() ?
                        this.mOverPeriod:
                        LocalDate.parse(req.getMOverPeriod(), DateTimeFormatter.ISO_DATE);

//        this.newItem =
//                req.getItemId() == null ?
//                        this.newItem :
//                        newItemRepository.findById(req.getItemId())
//                                .orElseThrow(ItemNotFoundException::new);

        this.produceOrganization =
                req.getProduceOrganizationId() == null ?
                        null :
                        produceOrganizationRepository.findById(req.getProduceOrganizationId() )
                                .orElseThrow(ProduceOrganizationNotFoundException::new);

        this.carType =
                req.getCarTypeId() == null ?
                        null :
                        carTypeRepository.findById(req.getCarTypeId())
                                .orElseThrow(CarTypeNotFoundException::new);


        //?????? ??????

        ProjectAttachmentUpdatedResult resultAttachment =

                findAttachmentUpdatedResult(
                        req.getAddedAttachments(),
                        req.getDeletedAttachments(),
                        false
                );

        if (req.getDeletedAttachments().size() > 0) {
            deleteProjectAttachments(
                    resultAttachment.getDeletedAttachments()
            );
        }

        if(oldTags.size()>0) {
            oldUpdatedAttachments(
                    oldTags,
                    oldComment,
                    targetAttaches,
                    attachmentTagRepository
            );
        }

        if (req.getAddedAttachments()!=null && req.getAddedAttachments().size()>0) {
            addUpdatedProjectAttachments(
                    newTags,
                    newComment,
                    resultAttachment.getAddedAttachments(),
                    attachmentTagRepository
            );
        }

        FileUpdatedResult fileUpdatedResult =
                new FileUpdatedResult(
                        resultAttachment//, updatedAddedProjectAttachmentList
                );

        this.modifier =
                memberRepository.findById(
                        req.getModifierId()
                ).orElseThrow(MemberNotFoundException::new);//05 -22 ????????? ??????

        ///0808 ??? ??????????????? ?????? ????????? ??????
//        if (req.getItemId().size()>0) {
//            this.newItems.clear();
//            this.newItems = new ArrayList<>();
//
//            addNewItems(req.getItemId().stream().map(
//                    id -> newItemRepository.findById(id).orElseThrow(ItemNotFoundException::new)
//            ).collect(toList()));
//        }

        if(tempEndOrUpdate.equals("tempEnd")){
            this.readonly = true;
            this.tempsave = true;
        }else{ // ?????? update ??????
            this.readonly = false;
            this.tempsave = true;
        }

        return fileUpdatedResult;
    }


    // update ??????

    /**
     * ????????? attachments
     */
    private void addProjectAttachments(
            List<ProjectAttachment> added) {
        added.forEach(i -> {
            projectAttachments.add(i);
            i.initProject(this);
        });
    }

    public void setTempsave(Boolean tempsave) {
        this.tempsave = tempsave;
    }

    public void setReadonly(Boolean readonly) {
        this.readonly = readonly;
    }

    public void updateTempSaveReadOnlyWhenMadeRoute() {
        this.tempsave = false;
        this.readonly = true;
    }

    // attachment ?????? ?????????

    private void addUpdatedProjectAttachments(
            List<Long> newTag,
            List<String> newComment,
            List<ProjectAttachment> added,
            AttachmentTagRepository attachmentTagRepository
    ) {
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();

        added.forEach(i -> {
            projectAttachments.add(i);
            i.initProject(this);

            i.setAttach_comment(
                    newComment.size()==0?" ":
                            newComment.get(
                                    (added.indexOf(i))
                            ).isBlank()?
                                    " ":newComment.get(
                                    (added.indexOf(i))
                            )
            );
            i.setTag(attachmentTagRepository
                    .findById(newTag.get(added.indexOf(i))).
                    orElseThrow(AttachmentNotFoundException::new).getName());
            i.setAttachmentaddress(
                    "src/main/prodmedia/image/" +
                            sdf1.format(now).substring(0, 10)
                            + "/"
                            + i.getUniqueName()
            );

        });


    }


    /**
     * ????????? ????????? ?????? (?????? ?????? ????????? ??????)
     *
     * @param deleted
     */
//    private void deleteProjectAttachments(List<ProjectAttachment> deleted) {
//        deleted.
//                forEach(di ->
//                        this.projectAttachments.remove(di)
//                );
//    }
    private void deleteProjectAttachments(List<ProjectAttachment> deleted) {
        // 1) save = false ??? ?????? ?????? ??? ??? ?????????

        for (ProjectAttachment att : deleted) {
            if (!att.isSave()) {
                this.projectAttachments.remove(att);
                //orphanRemoval=true??? ?????? Post???
                //?????? ????????? ???????????? ?????? ????????? ??? Image???
                // ??????????????????????????? ??????
            }
            // 2) save = true ??? ?????? ?????? ??? ????????? ?????? ??????
            else {
                att.setDeleted(true);
                att.setModifiedAt(LocalDateTime.now());
            }
        }
    }

    /**
     * ???????????? ?????? ??? ?????? ?????? ????????????
     *
     * @return
     */
    private ProjectAttachmentUpdatedResult findAttachmentUpdatedResult(
            List<MultipartFile> addedAttachmentFiles,
            List<Long> deletedAttachmentIds,
            boolean save
    ) {
        List<ProjectAttachment> addedAttachments
                = convertProjectAttachmentFilesToProjectAttachments(
                addedAttachmentFiles,
                save);
        List<ProjectAttachment> deletedAttachments
                = convertProjectAttachmentIdsToProjectAttachments(deletedAttachmentIds);

        addedAttachments.stream().forEach(
                i -> i.setSave(save)
        );

        return new ProjectAttachmentUpdatedResult(
                addedAttachmentFiles, addedAttachments, deletedAttachments);
    }


    private List<ProjectAttachment> convertProjectAttachmentIdsToProjectAttachments(List<Long> attachmentIds) {
        return attachmentIds.stream()
                .map(this::convertProjectAttachmentIdToProjectAttachment)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(toList());
    }

    private Optional<ProjectAttachment> convertProjectAttachmentIdToProjectAttachment(Long id) {
        return this.projectAttachments.stream().filter(i -> i.getId().equals(id)).findAny();
    }

    /**
     * ????????????
     *
     * @param attachmentFiles
     * @return
     */
    private List<ProjectAttachment> convertProjectAttachmentFilesToProjectAttachments(
            List<MultipartFile> attachmentFiles,
            boolean save
    ) {
        return attachmentFiles.stream().map(attachmentFile ->
                new ProjectAttachment(
                        attachmentFile.getOriginalFilename(),
                        save
                )).collect(toList());
    }

    /**
     * ???????????? ?????? ???????????? ????????? ????????? ???????????? ??????
     * ??? ?????? ???????????? ????????? ?????? ?????? ???????????????
     * ????????? ?????? ?????????, ????????? ?????? ?????? => ?????? ???????????? ??????
     */
    @Getter
    @AllArgsConstructor
    public static class ProjectAttachmentUpdatedResult {
        private List<MultipartFile> addedAttachmentFiles;
        private List<ProjectAttachment> addedAttachments;
        private List<ProjectAttachment> deletedAttachments;
    }

    @Getter
    @AllArgsConstructor
    public static class FileUpdatedResult {
        private ProjectAttachmentUpdatedResult attachmentUpdatedResult;
    }

    private void oldUpdatedAttachments
            (
                    List<Long> oldTag,
                    List<String> oldComment,
                    List<ProjectAttachment> olds, // ??? ???????????? ?????? old attachments ??? deleted ?????? ????????? ????????????
                    AttachmentTagRepository attachmentTagRepository
            ) {

        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date now = new Date();

        int ii=1;
        for(String old : oldComment) {

            System.out.println(old + " " + ii + " ??????");
            ii+=1;
        }
        int k=1;
        for(ProjectAttachment oldd : olds) {

            System.out.println(oldd.getOriginName() + " " + k + " ??????");
            k+=1;
        }

        olds.stream().forEach(i -> {

                    i.setAttach_comment(
                            oldComment.size()==0?
                                    " ":
                                    oldComment.get(
                                            (olds.indexOf(i))
                                    ).isBlank()?
                                            " ":oldComment.get(
                                            (olds.indexOf(i))
                                    )
                    );

                    i.setTag(attachmentTagRepository
                            .findById(oldTag.get(olds.indexOf(i))).
                            orElseThrow(AttachmentTagNotFoundException::new).getName());

                    i.setAttachmentaddress(
                            "src/main/prodmedia/image/" +
                                    sdf1.format(now).substring(0,10)
                                    + "/"
                                    + i.getUniqueName()
                    );

                }
        );

    }

    // drop pending delete

    public void updateLifeCycleWhenAllTheItemComplete() {
        this.lifecycle = "COMPLETE";
    }

    // update deleted
    public void updateDeleted(){
        this.deleted=!this.deleted;
    }

    // update drop
    public void updateDrop(){
        this.dropped=!this.dropped;
        this.lifecycle = "DROP";
    }

    // update pending
    public void updatePending(){
        this.pending= !this.pending;
        this.lifecycle = "PENDING";
    }

}
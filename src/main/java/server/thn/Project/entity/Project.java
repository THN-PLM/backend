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

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    // 삭제
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

    /**
     * 추가할 attachments
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

    public void updateTempsaveWhenMadeRoute() {
        this.tempsave = false;
    }

    // attachment 관련 메소드

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
     * 삭제될 이미지 제거 (고아 객체 이미지 제거)
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
        // 1) save = false 인 애들 지울 땐 찐 지우기

        for (ProjectAttachment att : deleted) {
            if (!att.isSave()) {
                this.projectAttachments.remove(att);
                //orphanRemoval=true에 의해 Post와
                //연관 관계가 끊어지며 고아 객체가 된 Image는
                // 데이터베이스에서도 제거
            }
            // 2) save = true 인 애들 지울 땐 아래와 같이 진행
            else {
                att.setDeleted(true);
                att.setModifiedAt(LocalDateTime.now());
            }
        }
    }

    /**
     * 업데이트 돼야 할 파일 정보 만들어줌
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
     * 어찌저찌
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
     * 업데이트 호출 유저에게 전달될 이미지 업데이트 결과
     * 이 정보 기반으로 유저는 실제 파일 저장소에서
     * 추가될 파일 업로드, 삭제할 파일 삭제 => 내역 남아있게 하기
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
                    List<ProjectAttachment> olds, // 이 아이템의 기존 old attachments 중 deleted 빼고 아이디 오름차순
                    AttachmentTagRepository attachmentTagRepository
            ) {

        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date now = new Date();

        int ii=1;
        for(String old : oldComment) {

            System.out.println(old + " " + ii + " 번째");
            ii+=1;
        }
        int k=1;
        for(ProjectAttachment oldd : olds) {

            System.out.println(oldd.getOriginName() + " " + k + " 번째");
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
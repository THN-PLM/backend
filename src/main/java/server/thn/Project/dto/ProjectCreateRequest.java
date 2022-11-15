package server.thn.Project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import server.thn.File.exception.AttachmentTagNotFoundException;
import server.thn.File.repository.AttachmentTagRepository;
import server.thn.Member.entity.Role;
import server.thn.Member.exception.MemberNotFoundException;
import server.thn.Member.repository.MemberRepository;
import server.thn.Project.entity.Project;
import server.thn.Project.entity.ProjectAttachment;
import server.thn.Project.entity.ProjectType;
import server.thn.Project.exception.BuyerOrganizationNotFoundException;
import server.thn.Project.exception.CarTypeNotFoundException;
import server.thn.Project.exception.ProduceOrganizationNotFoundException;
import server.thn.Project.exception.ProjectTypeNotFoundException;
import server.thn.Project.repository.ProjectTypeRepository;
import server.thn.Project.repository.buyer.BuyerOrganizationRepository;
import server.thn.Project.repository.carType.CarTypeRepository;
import server.thn.Project.repository.produceOrg.ProduceOrganizationRepository;

import javax.validation.constraints.Null;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Data
@NoArgsConstructor
@AllArgsConstructor
/**
 *  임시저장, 찐 저장 둘다 여기로 요청 형식을 맞춘다.
 */
public class ProjectCreateRequest {

    // @NotNull(message = "프로젝트 이름을 입력해주세요.")
    private String name;

    // @NotNull(message = "프로젝트 타입을 입력해주세요.")
    private Long projectTypeId;

    private String allDoStartPeriod;

    private String allDoOverPeriod;

    private String protoStartPeriod;

    private String protoOverPeriod;

    private String p1StartPeriod;

    private String p1OverPeriod;

    private String p2StartPeriod;

    private String p2OverPeriod;

    private String mStartPeriod;

    private String mOverPeriod;

    @Null // 로그인 된 멤버 자동 주입
    private Long memberId;

    private List<Long> productId;

    private List<MultipartFile> attachments = new ArrayList<>();

    private List<Long> tag = new ArrayList<>();

    private List<String> attachmentComment = new ArrayList<>();

    private Long buyerOrganizationId;

    private Long produceOrganizationId;

    private Long carTypeId;

    /**
     * String 으로 들어온 로컬데이트를 LocalDate 로 변환
     * @param str
     * @return
     */
    private static LocalDate strToLocalDate(String str){
        return str.isEmpty() ? null : LocalDate.parse(str, DateTimeFormatter.ISO_DATE);
    }

    /**
     * 찐저장
     * @param req
     * @param projectTypeRepository
     * @param memberRepository
     * @param produceOrganizationRepository
     * @param buyerOrganizationRepository
     * @param carTypeRepository
     * @param attachmentTagRepository
     * @return
     */
    public static Project toSaveEntity(
                ProjectCreateRequest req,
                ProjectTypeRepository projectTypeRepository,
                MemberRepository memberRepository,
                ProduceOrganizationRepository produceOrganizationRepository,
                BuyerOrganizationRepository buyerOrganizationRepository,
                CarTypeRepository carTypeRepository,
                AttachmentTagRepository attachmentTagRepository
    ){

        return new Project(
                projectTypeRepository.findById(req.getProjectTypeId()).orElseThrow(ProjectTypeNotFoundException::new),
                req.getName(),
                String.valueOf(req.getProjectTypeId() * 1000000 + (int) (Math.random() * 900000000)),
                strToLocalDate(req.getAllDoStartPeriod()),//all do
                strToLocalDate(req.getAllDoOverPeriod()),
                strToLocalDate(req.getAllDoStartPeriod()),//proto
                strToLocalDate(req.getAllDoOverPeriod()),
                strToLocalDate(req.getAllDoStartPeriod()),//p1
                strToLocalDate(req.getAllDoOverPeriod()),
                strToLocalDate(req.getAllDoStartPeriod()),//p2
                strToLocalDate(req.getAllDoOverPeriod()),
                strToLocalDate(req.getAllDoStartPeriod()),//m
                strToLocalDate(req.getAllDoOverPeriod()),
                memberRepository.findById(req.getMemberId()).orElseThrow(MemberNotFoundException::new), // creator
                true,
                true,
                produceOrganizationRepository.findById(req.getProduceOrganizationId()).orElseThrow(ProduceOrganizationNotFoundException::new),
                buyerOrganizationRepository.findById(req.getBuyerOrganizationId()).orElseThrow(BuyerOrganizationNotFoundException::new),

                !(req.getAttachments()==null)&&req.getTag().size()>0?req.attachments.stream().map(
                        i -> new ProjectAttachment(
                                i.getOriginalFilename(),
                                attachmentTagRepository
                                        .findById(req.getTag().get(req.attachments.indexOf(i))).
                                        orElseThrow(AttachmentTagNotFoundException::new).getName(),
                                req.getAttachmentComment().isEmpty() ?
                                        "" :
                                        req.getAttachmentComment().isEmpty() ?
                                                "" : req.getAttachmentComment().get(
                                                req.attachments.indexOf(i)
                                        ),
                                true
                        )
                ).collect(
                        toList()
                )
                : new ArrayList<>(), // 파일 없다면 빈 리스트로

                carTypeRepository.findById(req.getCarTypeId()).orElseThrow(CarTypeNotFoundException::new)
        );

    }

    public static Project toTempEntity(
            ProjectCreateRequest req,
            ProjectTypeRepository projectTypeRepository,
            MemberRepository memberRepository,
            ProduceOrganizationRepository produceOrganizationRepository,
            BuyerOrganizationRepository buyerOrganizationRepository,
            CarTypeRepository carTypeRepository,
            AttachmentTagRepository attachmentTagRepository
    ){

        return new Project(
                req.getProjectTypeId()==null?null: projectTypeRepository.findById(req.getProjectTypeId()).orElseThrow(ProjectTypeNotFoundException::new),
                (req.getName()==null||req.getName().isEmpty())? " " : req.getName(),
                "Made when Saved",

                req.getAllDoStartPeriod()==null?null:strToLocalDate(req.getAllDoStartPeriod()),//all do
                req.getAllDoOverPeriod()==null?null:strToLocalDate(req.getAllDoOverPeriod()),
                req.getAllDoStartPeriod()==null?null:strToLocalDate(req.getAllDoStartPeriod()),//all do
                req.getAllDoOverPeriod()==null?null:strToLocalDate(req.getAllDoOverPeriod()),
                req.getAllDoStartPeriod()==null?null:strToLocalDate(req.getAllDoStartPeriod()),//all do
                req.getAllDoOverPeriod()==null?null:strToLocalDate(req.getAllDoOverPeriod()),
                req.getAllDoStartPeriod()==null?null:strToLocalDate(req.getAllDoStartPeriod()),//all do
                req.getAllDoOverPeriod()==null?null:strToLocalDate(req.getAllDoOverPeriod()),
                req.getAllDoStartPeriod()==null?null:strToLocalDate(req.getAllDoStartPeriod()),//all do
                req.getAllDoOverPeriod()==null?null:strToLocalDate(req.getAllDoOverPeriod()),

                memberRepository.findById(req.getMemberId()).orElseThrow(MemberNotFoundException::new), // creator
                true, // 임시 저장은 true
                false, // 읽기 모드는 false
                req.getProduceOrganizationId()==null?null:produceOrganizationRepository.findById(req.getProduceOrganizationId()).orElseThrow(ProduceOrganizationNotFoundException::new),
                req.getBuyerOrganizationId()==null?null:buyerOrganizationRepository.findById(req.getBuyerOrganizationId()).orElseThrow(BuyerOrganizationNotFoundException::new),

                !(req.getAttachments()==null)&&req.getTag().size()>0?req.attachments.stream().map(
                        i -> new ProjectAttachment(
                                i.getOriginalFilename(),
                                attachmentTagRepository
                                        .findById(req.getTag().get(req.getAttachments().indexOf(i))).
                                        orElseThrow(AttachmentTagNotFoundException::new).getName(),
                                req.getAttachmentComment().isEmpty() ?
                                        "" :
                                        req.getAttachmentComment().isEmpty() ?
                                                "" : req.getAttachmentComment().get(
                                                req.getAttachments().indexOf(i)
                                        ),
                                true
                        )
                ).collect(
                        toList()
                ) :
                new ArrayList<>(),

                req.getCarTypeId()==null?null:carTypeRepository.findById(req.getCarTypeId()).orElseThrow(CarTypeNotFoundException::new)
        );

    }
}

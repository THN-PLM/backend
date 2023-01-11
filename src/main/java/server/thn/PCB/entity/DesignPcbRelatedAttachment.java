package server.thn.PCB.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import server.thn.Common.entity.EntityDate;
import server.thn.Member.exception.UnsupportedImageFormatException;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

@Setter
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DesignPcbRelatedAttachment extends EntityDate {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE2")
    @SequenceGenerator(name="SEQUENCE2", sequenceName="SEQUENCE2", allocationSize=1)
    private Long id;

    /**
     * 파일 구분용 이름
     */
    @Column(nullable = false)
    private String uniqueName;

    /**
     * 원래 파일 이름
     */
    @Column(nullable = false)
    private String originName;

    /**
     * 속하는 pcb 있을 시에만 파일 저장
     * pcb 사라지면 삭제됨
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pcb_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Pcb pcb;

    @Column(nullable = false)
    @Lob
    private String attach_comment;

    /**
     * deleted 여부
     */
    @Column(nullable = false)
    private boolean deleted;

    @Column(nullable = false)
    private boolean save;

    @Column(nullable = false)
    private String tag;

    @Column
    private String attachmentaddress;


    @Column
    //디폴트가 false, 따로 지정안하면 false 로 저장
    private boolean duplicate;


    /**
     * 지원하는 파일 확장자
     */
    private final static String supportedExtension[] =
            {"pdf", "hwp", "word", "docx", "ppt", "pptx"
                    ,"cmd:", "csv" , "doc", "dsc", "exe" ,
                    "xls", "xml", "xlc", "xlm", "txt", "zip"};

    /**
     * 각 파일의 고유명 생성 + 초기값 설정
     *
     * @param originName
     */
    public DesignPcbRelatedAttachment(
            String originName,
            String tag,
            String attach_comment,
            boolean save
    ) {
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();

        this.uniqueName =originName.replace("[","(").replace("]",")")+"-"+generateUniqueName(extractExtension(originName));
        this.originName = originName;
        this.tag = tag;
        this.attach_comment = attach_comment;
        this.attachmentaddress =
                "src/main/prodmedia/image/" +
                        sdf1.format(now).substring(0,10)
                        + "/"
                        +this.originName + "-"
                        + this.uniqueName;
        this.save = save;
    }

    /**
     * duplicated file 생성
     * (기존 것 다 우려먹지만,
     * 새롭게 id 생성되고 이름도 다르게 ㅋ)
     *이때 특이한 것은 duplicate = true 로 갱신 !
     * @param originName
     */
    public DesignPcbRelatedAttachment(
            String originName,
            String attachmentaddress,
            boolean save,
            String tag,
            String attach_comment
    ) {
        this.uniqueName = originName.replace("[","(").replace("]",")")+"-"+generateUniqueName(extractExtension(originName));
        this.originName = originName;
        this.attachmentaddress = attachmentaddress; // 기존 것 베끼기
        this.save = save;
        this.duplicate = true;

        this.tag = tag;
        this.attach_comment = attach_comment;
    }

    /**
     * 각 이미지의 고유명 생성
     *
     * @param originName
     */
    public DesignPcbRelatedAttachment(String originName) {

        this.uniqueName =originName.replace("[","(").replace("]",")")+"-"+generateUniqueName(extractExtension(originName));
        this.originName = originName;
    }

    /**
     * pcb 연관관계가 없다면 등록
     *
     * @param pcb
     */
    public void initPcb(Pcb pcb) {

        if (this.pcb == null) {
            this.pcb = pcb;
        }

    }

    /**
     * 이미지 저장될 공간
     *
     * @param extension
     * @return
     */
    private String generateUniqueName(String extension) {
        return UUID.randomUUID() + "." + extension;
    }

    /**
     * 확장자 확인
     *
     * @param originName
     * @return
     */
    private String extractExtension(String originName) {
        try {
            
            String ext =
                    originName.substring(
                            originName.lastIndexOf(".") + 1
                    );
            return ext;
        } catch (StringIndexOutOfBoundsException e) {
        }
        throw new UnsupportedImageFormatException();
    }

    /**
     * 지원하는 형식인지 확인(이미지 파일)
     *
     * @param ext
     * @return
     */
    private boolean isSupportedFormat(String ext) {

        return Arrays.stream(supportedExtension)
                .anyMatch(e -> e.equalsIgnoreCase(ext));

    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public void setSave(boolean save) {
        this.save = save;
    }

}

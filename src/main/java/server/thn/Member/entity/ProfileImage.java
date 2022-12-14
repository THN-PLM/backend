package server.thn.Member.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import server.thn.Common.entity.EntityDate;
import server.thn.Member.exception.UnsupportedImageFormatException;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProfileImage extends EntityDate {

    @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE1")
    @SequenceGenerator(name="SEQUENCE1", sequenceName="SEQUENCE1", allocationSize=1)

    private Long id;

    /**
     *  파일 구분용 이름
     */
    @Column(nullable = false)
    private String uniqueName;

    /**
     * 원래 파일 이름
     */
    @Column(nullable = false)
    private String originName;

    @Column
    private String imageaddress;

    /**
     * 속하는 아이템이 있을 시에만 이미지 저장
     * 아이템 사라지면 삭제됨
     */

    /**
     * 멤버가 사라진다면
     * 프로필 이미지도 사라진다
     */
    @OneToOne(
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @JoinColumn(name = "member", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    /**
     * 지원하는 이미지 확장자
     */
    private final static String supportedExtension[] =
            {"jpg", "jpeg", "gif", "bmp", "png"};

    /**
     * 각 이미지의 고유명 생성 + 초기값 설정
     * @param originName
     */
    public ProfileImage(String originName) {
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();

        this.uniqueName = generateUniqueName(extractExtension(originName));
        this.originName = originName;
        this.imageaddress =
                "src/main/prodmedia/image/" +
                        sdf1.format(now).substring(0,10)
                        + "/"
                        + this.uniqueName; //이미지 저장 폴더 + 이미지 저장명
    }

    /**
     * 아이템과 연관관계가 없다면 등록
     * @param member
     */
    public void initMember(Member member) {
        if(this.member == null) {
            this.member = member;
        }
    }

    /**
     * 이미지 저장될 공간
     * @param extension
     * @return
     */
    private String generateUniqueName(String extension) {
        return UUID.randomUUID().toString() + "." + extension;
    }

    /**
     * 확장자 확인
     * @param originName
     * @return
     */
    private String extractExtension(String originName) {
        try {
            String ext = originName.substring(originName.lastIndexOf(".") + 1);
            if(isSupportedFormat(ext)) return ext;
        } catch (StringIndexOutOfBoundsException e) { }
        throw new UnsupportedImageFormatException();
    }

    /**
     * 지원하는 형식인지 확인(이미지 파일)
     * @param ext
     * @return
     */
    private boolean isSupportedFormat(String ext) {
        return Arrays.stream(supportedExtension).anyMatch(e -> e.equalsIgnoreCase(ext));
    }

}

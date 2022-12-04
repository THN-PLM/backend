package server.thn.Member.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import server.thn.Common.entity.EntityDate;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Entity
@Getter
@Table(name="member")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends EntityDate {

    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE3")
    @SequenceGenerator(name="SEQUENCE3", sequenceName="SEQUENCE3", allocationSize=1)

    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false, length = 30, unique = true)
    private String email;

    private String password;

    @Column(nullable = false, length = 20)
    private String username;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "t1_id", nullable = false)
//    @JoinColumn(name = "t2_id", nullable = false)
//    @JoinColumn(name = "t3_id", nullable = false)
//    @JoinColumn(name = "t4_id", nullable = false)
//    private TeamClassification department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Department department;

    @Column(nullable = false, length = 20)
    private String contact;

    @OneToMany(
            mappedBy = "member",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private Set<MemberRole> roles;

    @OneToOne(
            mappedBy = "member",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            optional = false
    )
    @JoinColumn(name = "profile_image")
    private ProfileImage profileImage;

    public Member(
            String email,
            String password,
            String username,
            //TeamClassification department,
            Department department,
            String contact,
            List<Role> roles,
            ProfileImage profileImage
    ) {

        this.email = email;
        this.password = password;
        this.username = username;
        this.department = department;
        this.contact = contact;
        this.roles =
                roles.stream().map(r -> new MemberRole(
                                this, r))
                        .collect(toSet());
        this.profileImage = profileImage;
        addProfileImages(profileImage);
    }

    /**
     * profile image가 없을 때
     * @param email
     * @param password
     * @param username
     * @param department
     * @param contact
     * @param roles
     */
    public Member(
            String email,
            String password,
            String username,
            //TeamClassification department,
            Department department,
            String contact,
            List<Role> roles
    ) {

        this.email = email;
        this.password = password;
        this.username = username;
        this.department = department;
        this.contact = contact;
        this.roles =
                roles.stream().map(r -> new MemberRole(
                                this, r))
                        .collect(toSet());
        this.profileImage = null;
    }

    /**
     * 멤버에 새로운 이미지 정보를 등록하는 메소드
     * 해당 Image에 this(Member)를 등록해줍니다.
     * cascade 옵션을 PERSIST로 설정해두었기 때문에,
     * Post가 저장되면서 Image도 함께 저장
     * @param added
     */
    private void addProfileImages(ProfileImage added) {
        added.initMember(this);
    }

}

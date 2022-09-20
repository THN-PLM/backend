package server.thn.Member.dto.sign;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;
import server.thn.Member.entity.Member;
import server.thn.Member.entity.ProfileImage;
import server.thn.Member.entity.teamClassification.TeamClassification;
import server.thn.Member.exception.ClassificationNotFoundException;
import server.thn.Member.exception.RoleNotFoundException;
import server.thn.Member.repository.RoleRepository;
import server.thn.Member.repository.classification.TeamClassification1Repository;
import server.thn.Member.repository.classification.TeamClassification2Repository;
import server.thn.Member.repository.classification.TeamClassification3Repository;
import server.thn.Member.repository.classification.TeamClassification4Repository;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest {

    private String email;

    private String password;

    private String username;

    private Long department1Id;

    private Long department2Id;

    private Long department3Id;

    private Long department4Id;

    private String contact;

    @Nullable
    private MultipartFile profileImage;

    private List<Long> positionId;

    public static Member toEntity(
            SignUpRequest req,
            //Role role,
            PasswordEncoder encoder,
            RoleRepository roleRepository,
            TeamClassification1Repository teamClassification1Repository,
            TeamClassification2Repository teamClassification2Repository,
            TeamClassification3Repository teamClassification3Repository,
            TeamClassification4Repository teamClassification4Repository
    ) {

        if(!(req.getProfileImage()==null)) {
            return new Member
                    (
                            req.email,
                            encoder.encode(req.password),
                            req.username,
                            new TeamClassification(
                                    teamClassification1Repository.findById(req.department1Id).orElseThrow(ClassificationNotFoundException::new),
                                    teamClassification2Repository.findById(req.department2Id).orElseThrow(ClassificationNotFoundException::new),
                                    teamClassification3Repository.findById(req.department3Id).orElseThrow(ClassificationNotFoundException::new),
                                    teamClassification4Repository.findById(req.department4Id).orElseThrow(ClassificationNotFoundException::new)
                            ),
                            req.contact,

                            req.getPositionId().stream().map(
                                    id ->
                                            roleRepository.findById(id).orElseThrow(RoleNotFoundException::new)
                            ).collect(Collectors.toList()),

                            new ProfileImage(
                                    req.profileImage.
                                            getOriginalFilename()
                            )

                    );
        }else{
            return new Member
                    (
                            req.email,
                            encoder.encode(req.password),
                            req.username,
                            new TeamClassification(
                                    teamClassification1Repository.findById(req.department1Id).orElseThrow(ClassificationNotFoundException::new),
                                    teamClassification2Repository.findById(req.department2Id).orElseThrow(ClassificationNotFoundException::new),
                                    teamClassification3Repository.findById(req.department3Id).orElseThrow(ClassificationNotFoundException::new),
                                    teamClassification4Repository.findById(req.department4Id).orElseThrow(ClassificationNotFoundException::new)
                            ),
                            req.contact,

                            req.getPositionId().stream().map(
                                    id ->
                                            roleRepository.findById(id).orElseThrow((RoleNotFoundException::new))
                            ).collect(Collectors.toList())

                    );
        }
    }
}
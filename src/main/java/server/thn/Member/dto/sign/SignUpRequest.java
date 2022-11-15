package server.thn.Member.dto.sign;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;
import server.thn.Member.entity.Member;
import server.thn.Member.entity.ProfileImage;
import server.thn.Member.exception.ClassificationNotFoundException;
import server.thn.Member.exception.RoleNotFoundException;
import server.thn.Member.repository.DepartmentRepository;
import server.thn.Member.repository.RoleRepository;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest {

    private String email;

    private String password;

    private String username;

    private Long departmentId;

    private String contact;

    @Nullable
    private MultipartFile profileImage;

    private List<Long> positionId;

    public static Member toEntity(
            SignUpRequest req,
            PasswordEncoder encoder,
            RoleRepository roleRepository,
            DepartmentRepository departmentRepository
    ) {

        if(!(req.getProfileImage()==null)) {
            return new Member
                    (
                            req.email,
                            encoder.encode(req.password),
                            req.username,
                            departmentRepository
                                    .findById(req.getDepartmentId())
                                    .orElseThrow(ClassificationNotFoundException::new),
                            req.contact,

                            req.getPositionId().stream().map(
                                    id ->
                                            roleRepository.findById(id).orElseThrow(
                                                    RoleNotFoundException::new)
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
                            departmentRepository
                                    .findById(req.getDepartmentId())
                                    .orElseThrow(ClassificationNotFoundException::new),
                            req.contact,

                            req.getPositionId().stream().map(
                                    id ->
                                            roleRepository.findById(id).orElseThrow((RoleNotFoundException::new))
                            ).collect(Collectors.toList())

                    );
        }
    }
}
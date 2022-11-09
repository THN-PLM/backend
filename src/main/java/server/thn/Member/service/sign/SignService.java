package server.thn.Member.service.sign;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import server.thn.File.service.FileService;
import server.thn.Member.dto.MemberDto;
import server.thn.Member.dto.sign.RefreshTokenResponse;
import server.thn.Member.dto.sign.SignInRequest;
import server.thn.Member.dto.sign.SignInResponse;
import server.thn.Member.dto.sign.SignUpRequest;
import server.thn.Member.entity.Member;
import server.thn.Member.entity.ProfileImage;
import server.thn.Member.exception.MemberEmailAlreadyExistsException;
import server.thn.Member.exception.MemberNotFoundException;
import server.thn.Member.exception.PasswordNotValidateException;
import server.thn.Member.exception.RefreshExpiredException;
import server.thn.Member.repository.DepartmentRepository;
import server.thn.Member.repository.MemberRepository;
import server.thn.Member.repository.RoleRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
/**
 * 액세스 토큰 / 리프레시 토큰발급
 */
public class SignService {

    private final FileService fileService;
    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final DepartmentRepository departmentRepository;

    @Value("${default.image.address}")
    private String defaultImageAddress;

    @Transactional
    public void signUp(SignUpRequest req) {
        validateSignUpInfo(req);
        Member member = memberRepository.save(
                SignUpRequest.toEntity(
                        req,
                        passwordEncoder,
                        roleRepository,
                        departmentRepository
                )
        );
        if(req.getProfileImage()!=null){
            uploadProfileImage(member.getProfileImage(), req.getProfileImage());
        }
    }

    private void uploadProfileImage(ProfileImage profileImage, MultipartFile fileImage) {
        fileService.upload(fileImage, profileImage.getUniqueName()
        );
    }

    @Transactional(readOnly = true)
    public SignInResponse signIn(SignInRequest req) {



        Member member = memberRepository.findByEmail(req.getEmail()).orElseThrow(MemberNotFoundException::new);

        validatePassword(req, member);
        String subject = createSubject(member);
        String accessToken = tokenService.createAccessToken(subject);
        String refreshToken = tokenService.createRefreshToken(subject);
        MemberDto member1 = MemberDto.toDto
                (memberRepository.
                                findById(member.getId()).orElseThrow(MemberNotFoundException::new),
                        defaultImageAddress);

        return new SignInResponse(accessToken, refreshToken, member1);

    }

    private void validateSignUpInfo(SignUpRequest req) {
        if(memberRepository.existsByEmail(req.getEmail()))
            throw new MemberEmailAlreadyExistsException();
    }

    private void validatePassword(SignInRequest req, Member member) {
        if(!passwordEncoder.matches(req.getPassword(), member.getPassword())) {
            throw new PasswordNotValidateException();
        }
    }

    private String createSubject(Member member) {
        return String.valueOf(member.getId());
    }

    /**
     * 검증된 리프레시 토큰이라면 액세스 재발급해서 돌려주기
     */
    public RefreshTokenResponse refreshToken(String rToken) {
    /*
    리프레시 토큰 검증 후 리프레시 유효하면 새로운 access 돌려주기
    유효하지 않다면 validateRefreshToken 에서 에러 던짐
     */
        validateRefreshToken(rToken);
        String subject = tokenService.extractRefreshTokenSubject(rToken);
        String accessToken = tokenService.createAccessToken(subject);
        return new RefreshTokenResponse(accessToken);

    }

    /**
     * 리프레시 토큰이 유효하지 않다면 401 에러
     */
    private void validateRefreshToken(String rToken) {

        if(!tokenService.validateRefreshToken(rToken)) {
            throw new RefreshExpiredException();
        }
    }

}

package server.thn.Member.dto.sign;

import lombok.AllArgsConstructor;
import lombok.Data;
import server.thn.Member.dto.MemberDto;

@Data
@AllArgsConstructor
public class SignInResponse {
    private String accessToken;
    private String refreshToken;
    private MemberDto member;
}
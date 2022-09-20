package server.thn.Member.dto.sign;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 액세스 토큰 가져옴
 */
@Data
@AllArgsConstructor
public class RefreshTokenResponse {
    private String accessToken;
}

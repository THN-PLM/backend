package server.thn.Member.service.sign;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import server.thn.Member.exception.AccessExpiredException;
import server.thn.Member.handler.JwtHandler;

@Service
@RequiredArgsConstructor
public class TokenService {
    /**
     * JwtHandler를 이용하여
     * 각각 토큰 종류마다,
     * 검증 & subject 추출
     */
    private final JwtHandler jwtHandler;

    @Value("${jwt.max-age.access}")
    private long accessTokenMaxAgeSeconds;

    @Value("${jwt.max-age.refresh}")
    private long refreshTokenMaxAgeSeconds;

    @Value("${jwt.key.access}")
    private String accessKey;

    @Value("${jwt.key.refresh}")
    private String refreshKey;

    public String createAccessToken(String subject) {
        return jwtHandler.createToken(
                accessKey,
                subject,
                accessTokenMaxAgeSeconds
        );
    }

    public String createRefreshToken(String subject) {
        return jwtHandler.createToken(
                refreshKey,
                subject,
                refreshTokenMaxAgeSeconds
        );
    }

    public boolean validateAccessToken(String token) {
        return jwtHandler.validate(accessKey, token);
    }

    public boolean validateRefreshToken(String token) {
        return jwtHandler.validate(refreshKey, token);
    }

    public String extractAccessTokenSubject(String token) {
        return jwtHandler.extractSubject(accessKey, token);
    }

    public String extractRefreshTokenSubject(String token) {
        return jwtHandler.extractSubject(refreshKey, token);
    }

    public void accessTokenExpired(){
        throw new AccessExpiredException();
    }
}

package server.thn.Member.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.*;
import server.thn.Common.dto.response.Response;
import server.thn.Member.dto.MemberDto;
import server.thn.Member.dto.sign.SignInRequest;
import server.thn.Member.dto.sign.SignInResponse;
import server.thn.Member.dto.sign.SignUpRequest;
import server.thn.Member.service.sign.SignService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URLEncoder;

import static server.thn.Common.dto.response.Response.success;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "https://localhost:3000")
public class SignController {
    private final SignService signService;

    @CrossOrigin(origins = "https://localhost:3000")
    @PostMapping("sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "회원가입", notes = "회원가입")
    public Response signUp(@Valid SignUpRequest req) {
        signService.signUp(req);
        return success();
    }

    @CrossOrigin(origins = "https://localhost:3000")
    @PostMapping("/sign-in")
    @ApiOperation(value = "로그인", notes = "로그인")
    @ResponseBody
    public Response signIn(@Valid SignInRequest req, HttpServletResponse response) throws IOException {
        SignInResponse signInResponse = signService.signIn(req);
        String refreshToken = signInResponse.getRefreshToken();
        String accessToken = signInResponse.getAccessToken();
        MemberDto memberDto = signInResponse.getMember();
        refreshToken = URLEncoder.encode(refreshToken, "utf-8");

        Cookie refreshCookie = new Cookie("refreshToken", refreshToken);

        refreshCookie.setMaxAge(7 * 24 * 60 * 60);
        refreshCookie.setSecure(true);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setPath("/");
        response.addCookie(refreshCookie);

        ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
                .maxAge(7 * 24 * 60 * 60)
                .path("/")
                .secure(true)
                .sameSite("None")
                .httpOnly(true)
                .build();
        response.setHeader("Set-Cookie", cookie.toString());

        return success(new SignInResponse(accessToken, "httponly", memberDto));
    }

    /**
     * 토큰 리프레쉬 api
     * <p>
     * HTTP Authorization 헤더에 리프레시 토큰을 전달
     * SignService.refreshToken 수행
     *
     * @param refreshToken
     * @return success
     */

    @CrossOrigin(origins = "https://localhost:3000")
    @PostMapping("/refresh-token")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "refresh token 발급 ", notes = "refresh token 발급")
    public Response refreshToken(@RequestHeader(value = "cookie") String refreshToken) {
        System.out.println(refreshToken);
        Integer index = refreshToken.length();

        String rToken = (refreshToken.toString().substring(20, index));

        Response response = success(signService.refreshToken("Bearer " + rToken));

        System.out.println("access token:" + signService.refreshToken("Bearer " + rToken).getAccessToken());
        System.out.println("access token string ver:" + signService.refreshToken("Bearer " + rToken).toString());
        System.out.println("code:" + response.getCode());
        System.out.println("result" + response.getResult());

        return response;
    }


    @CrossOrigin(origins = "https://localhost:3000")
    @PostMapping("/logout/{id}")
    @ResponseBody
    @ApiOperation(value = "로그아웃 ", notes = "로그아웃")
    public Response signOut(@PathVariable Long id) {
        return success(id);
    }

}
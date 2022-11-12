package server.thn.Advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import server.thn.Common.dto.response.Response;
import server.thn.File.exception.AttachmentNotFoundException;
import server.thn.File.exception.AttachmentTagNotFoundException;
import server.thn.File.exception.FileUploadFailureException;
import server.thn.Member.exception.*;
import server.thn.Project.exception.CarTypeNotFoundException;
import server.thn.Project.exception.ProduceOrganizationNotFoundException;
import server.thn.Project.exception.ProjectNotFoundException;

@RestControllerAdvice
@Slf4j
public class ExceptionAdvice {

    /**
     * 로그인 안됐을 시 에러
     * @return
     */
    @ExceptionHandler(AuthenticationEntryPointException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Response authenticationEntryPoint() {
        return Response.failure(401, "리프레시가 필요합니다.");
    }

    /**
     * 접근 권한 없음
     * @return 403
     */

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Response accessDeniedException() {
        return Response.failure(403, "access denied : 접근이 거부되었습니다.");
    }


    /**
     * 헤더 누락 시 에러
     * @param e
     * @return 400
     */
    @ExceptionHandler(MissingRequestHeaderException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response missingRequestHeaderException(MissingRequestHeaderException e) {
        return Response.failure(400, " header missed : "+ e.getHeaderName() + " 요청 헤더가 누락되었습니다.");
    }


    @ExceptionHandler(ClassificationNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response ClassificationNotFoundException() {
        //채워지지 않은 항목 존재 (저장 시에)
        return Response.failure(404, "no classification found");
    }

    @ExceptionHandler(AttachmentNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response AttachmentNotFoundException() {
        return Response.failure(404, "AttachmentNotFoundException.");
    }

    @ExceptionHandler(FileUploadFailureException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response FileUploadFailureException() {
        return Response.failure(404, "FileUploadFailureException");
    }

    @ExceptionHandler(AttachmentTagNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response AttachmentTagNotFoundException() {
        return Response.failure(404, "AttachmentTagNotFoundException");
    }

    @ExceptionHandler(CarTypeNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response CarTypeNotFoundException() {
        return Response.failure(404, "CarTypeNotFoundException");
    }
    @ExceptionHandler(ProduceOrganizationNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response ProduceOrganizationNotFoundException() {
        return Response.failure(404, "ProduceOrganizationNotFoundException");
    }

    @ExceptionHandler(MemberEmailAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response MemberEmailAlreadyExistsException() {
        return Response.failure(400, "MemberEmailAlreadyExistsException");
    }

    @ExceptionHandler(MemberNotAssignedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response MemberNotAssignedException() {
        return Response.failure(404, "MemberNotAssignedException");
    }

    @ExceptionHandler(MemberNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response MemberNotFoundException() {
        return Response.failure(404, "MemberNotFoundException");
    }

    @ExceptionHandler(MemberOverAssignedException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response MemberOverAssignedException() {
        return Response.failure(404, "MemberOverAssignedException");
    }

    @ExceptionHandler(PasswordNotValidateException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Response passwordNotValidateException() {
        return Response.failure(401, "비밀번호가 틀렸습니다.");
    }

    /**
     * 리프레시 토큰이 유효하지 않을 때 에러
     * @return
     */
    @ExceptionHandler(RefreshExpiredException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Response refreshExpiredException() {
        return Response.failure(401, "리프레쉬 재발급이 필요합니다.");
    }

    @ExceptionHandler(RoleNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response roleNotFoundException() {
        return Response.failure(404, "Requested rating not available.");

    }

    @ExceptionHandler(UnsupportedImageFormatException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response UnsupportedImageFormatException(UnsupportedImageFormatException e) {
        //log.info("e = {}", e.getMessage());
        return Response.failure(400,
                "png jpg only - 이미지(png,jpg) 형식의 파일을 업로드해주세요.");
    }

}

package server.thn.File.service;

import org.springframework.context.annotation.Primary;
import org.springframework.web.multipart.MultipartFile;

/**
 * 파일 업로드 & 삭제 수행 인터페이스
 */

@Primary
public interface FileService {

    void upload(MultipartFile file, String filename);
    void delete(String filename);

}

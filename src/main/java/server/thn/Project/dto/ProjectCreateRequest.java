package server.thn.Project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectCreateRequest {

    @NotNull(message = "프로젝트 이름을 입력해주세요.")
    private String name;

    @NotNull(message = "프로젝트 타입을 입력해주세요.")
    private Long projectTypeId;

    private String allDoStartPeriod;

    private String allDoOverPeriod;

    private String protoStartPeriod;

    private String protoOverPeriod;

    private String p1StartPeriod;

    private String p1OverPeriod;

    private String p2StartPeriod;

    private String p2OverPeriod;

    private String mStartPeriod;

    private String mOverPeriod;

    @Null // 로그인 된 멤버 자동 주입
    private Long memberId;

    private List<Long> productId;

    private List<MultipartFile> attachments = new ArrayList<>();

    private List<Long> tag = new ArrayList<>();

    private List<String> attachmentComment = new ArrayList<>();

    private Long projectLevelId;

    private Long buyerOrganizationId;

    private Long supplierId;

    private Long carTypeId;

}

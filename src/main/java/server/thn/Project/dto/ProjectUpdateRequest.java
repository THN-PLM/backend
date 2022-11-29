package server.thn.Project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Null;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectUpdateRequest {

    private String name;

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

    @Null
    private Long modifierId;

    private Long buyerOrganizationId;

    private Long produceOrganizationId;

    private Long carTypeId;

    /**
     * 추가된 파일을 첨부
     */
    private List<MultipartFile> addedAttachments = new ArrayList<>();
    private List<Long> addedTag = new ArrayList<>();
    private List<String> addedAttachmentComment = new ArrayList<>();

    /**
     * 삭제될 파일 아이디 입력 - is deleted 만 true
     */
    private List<Long> deletedAttachments = new ArrayList<>();

    // private List<Long> productId;
    // private List<Long> deletedProductId = new ArrayList<>();

}

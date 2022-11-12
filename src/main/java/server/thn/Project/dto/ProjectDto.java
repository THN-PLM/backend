package server.thn.Project.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;import server.thn.Project.dto.produceOrg.ProduceOrganizationDto;
import server.thn.Project.entity.Project;
import server.thn.Project.entity.ProjectAttachment;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDto {

    private Long id;

    private String number;

    private String name;

    private String type;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate endDate;

    private ProduceOrganizationDto produceOrganization;

    private ProduceOrganizationDto buyer;

    private String phase; // 라우트 단계 이름

    private String status; // period 랑 똑같음

    private String period; // 현재 진행 중인 라우트 시점

    // List<ProductDto> project 에 속한 product 리스트

    private List<ProjectAttachment> projectAttachmentList;

//    public static ProjectDto toDto(
//            Project project
//    ){
//        return new ProjectDto(
//                project.getId(),
//                project.getName(),
//                project.getProjectType().getProjectType().name(),
//                // project.getA
//                // 라우트로 현재 단계 판별 뒤, 이에 해당하는 시기 날짜 건네주면 된다.
//
//        );
//    }
}


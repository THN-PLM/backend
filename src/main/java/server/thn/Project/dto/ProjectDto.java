package server.thn.Project.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import server.thn.File.repository.AttachmentTagRepository;
import server.thn.Project.dto.produceOrg.ProduceOrganizationDto;
import server.thn.Project.entity.Project;
import server.thn.Project.entity.ProjectAttachment;
import server.thn.Route.entity.RouteOrdering;
import server.thn.Route.repository.RouteOrderingRepository;
import server.thn.Route.repository.RouteProductRepository;

import javax.persistence.Column;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDto {

    private Long id;

    private String number;

    private String name;

    private String type;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate allDoStartPeriod;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate allDoOverPeriod;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate protoStartPeriod;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate protoOverPeriod;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate p1StartPeriod;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate p1OverPeriod;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate p2StartPeriod;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate p2OverPeriod;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate mStartPeriod;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate mOverPeriod;

    private ProduceOrganizationDto produceOrganization;

    private ProduceOrganizationDto buyer;

    private String phase; // 라우트 단계 이름

    private String status; // period 랑 똑같음

    private String period; // 현재 진행 중인 라우트 시점

    //List<ProductDto> project 에 속한 product 리스트

    private List<ProjectAttachmentDto> projectAttachmentList;

    public static ProjectDto toDto(
            Project project,
            RouteOrderingRepository routeOrderingRepository,
            RouteProductRepository routeProductRepository,
            AttachmentTagRepository attachmentTagRepository
    ){


        List<ProjectAttachmentDto> attachmentDtoList = new ArrayList<>();

        if(project.getProjectAttachments()!=null) {
            List<ProjectAttachmentDto> projectAttachments = project.getProjectAttachments().
                    stream().
                    map(i ->
                            ProjectAttachmentDto.toDto
                                    (i, attachmentTagRepository)
                    )
                    .collect(toList());

            attachmentDtoList
                    .addAll(projectAttachments);

            Collections.sort(attachmentDtoList);
        }

        return new ProjectDto(
                project.getId(),
                project.getProjectNumber(),
                project.getName(),
                project.getProjectType().getId().toString(),

                project.getAllDoStartPeriod(),
                project.getAllDoOverPeriod(),
                project.getProtoStartPeriod(),
                project.getProtoOverPeriod(),
                project.getP1StartPeriod(),
                project.getP1OverPeriod(),
                project.getP2StartPeriod(),
                project.getP2OverPeriod(),
                project.getMStartPeriod(),
                project.getMOverPeriod(),

                ProduceOrganizationDto.toDto(project.getProduceOrganization()),
                ProduceOrganizationDto.toDto(project.getBuyerOrganization()),
                " ",
                " ",
                " ",
                attachmentDtoList
                // 라우트로 현재 단계 판별 뒤, 이에 해당하는 시기 날짜 건네주면 된다.
        );
    }
}


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
import server.thn.Route.entity.RouteProduct;
import server.thn.Route.repository.RouteOrderingRepository;
import server.thn.Route.repository.RouteProductRepository;

import javax.persistence.Column;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

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

    private boolean readonly;

    private boolean tempsave;

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

        // project 에 딸린 라우트가 있다면
        RouteProduct currentRouteProduct = null;

        if(routeOrderingRepository.findByProjectOrderByIdAsc(project).size()>0) {
            RouteOrdering routeOrdering = routeOrderingRepository.findByProjectOrderByIdAsc(project).get(
                    routeOrderingRepository.findByProjectOrderByIdAsc(project).size() - 1
            );

            List<RouteProduct> routeProductList =
                    routeProductRepository.findAllByRouteOrdering(routeOrdering);

            if (!(routeOrdering.getPresent() == routeProductList.size())) {
                currentRouteProduct = routeProductList.get(routeOrdering.getPresent());
            }
        }

        String[] status= currentRouteProduct.getRoute_name().split(" ");

        return new ProjectDto(
                project.getId(),
                project.getProjectNumber()==null?" ":project.getProjectNumber(),
                project.getName()==null?" ":project.getName(),
                project.getProjectType().getId().toString()==null?" ":project.getProjectType().getId().toString(),

                project.getAllDoStartPeriod()==null? LocalDate.parse(" ") : project.getAllDoStartPeriod(),
                project.getAllDoOverPeriod()==null? LocalDate.parse(" ") : project.getAllDoOverPeriod(),
                project.getProtoStartPeriod()==null? LocalDate.parse(" ") : project.getProtoStartPeriod(),
                project.getProtoOverPeriod()==null? LocalDate.parse(" "): project.getProtoOverPeriod(),
                project.getP1StartPeriod()==null?LocalDate.parse(" "): project.getP1StartPeriod(),
                project.getP1OverPeriod()==null?LocalDate.parse(" "): project.getP1OverPeriod(),
                project.getP2StartPeriod()==null?LocalDate.parse(" "): project.getP2StartPeriod(),
                project.getP2OverPeriod()==null?LocalDate.parse(" "): project.getP2OverPeriod(),
                project.getMStartPeriod()==null?LocalDate.parse(" "): project.getMStartPeriod(),
                project.getMOverPeriod()==null?LocalDate.parse(" "): project.getMOverPeriod(),

                project.getProduceOrganization()==null?new ProduceOrganizationDto():ProduceOrganizationDto.toDto(project.getProduceOrganization()),
                project.getBuyerOrganization()==null?new ProduceOrganizationDto():ProduceOrganizationDto.toDto(project.getBuyerOrganization()),
                currentRouteProduct==null? " " : currentRouteProduct.getRoute_name(), // phase
                currentRouteProduct==null? " " : status.length>=2?status[1]:currentRouteProduct.getRoute_name(), //status
                currentRouteProduct==null? " " : status.length>=2?status[0]:currentRouteProduct.getRoute_name(), //period
                attachmentDtoList,
                // 라우트로 현재 단계 판별 뒤, 이에 해당하는 시기 날짜 건네주면 된다.

                project.getReadonly(),
                project.getTempsave()
        );
    }
}


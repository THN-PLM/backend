package server.thn.Route.dto.routeOrdering;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import server.thn.Route.dto.routeProduct.RouteProductDto;
import server.thn.Route.entity.RouteOrdering;
import server.thn.Route.repository.RouteOrderingRepository;
import server.thn.Route.repository.RouteProductRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RouteOrderingDto {

    private Long id;
    private String type;
    private String lifecycleStatus;
    private String workflowphase;
    private Integer revisedCnt;
    private Integer present;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;
    private List<RouteProductDto> routeProductList;

    //05-24 추가
    private RouteRejectPossibleResponse response;


    public static List <RouteOrderingDto> toDtoList(
            List <RouteOrdering> NewRoutes,
            RouteProductRepository routeProductRepository,
            RouteOrderingRepository routeOrderingRepository,
            String defaultImageAddress
    ) {

        List<SeqAndName> tmpList = new ArrayList<>();
        RouteRejectPossibleResponse tmpRouteRejectPossibleResponse = new RouteRejectPossibleResponse(tmpList);

        List<RouteOrderingDto> newRouteDtos = NewRoutes.stream().map(
                c -> new RouteOrderingDto(
                        c.getId(),
                        c.getType(),
                        c.getLifecycleStatus(),
                        c.getLifecycleStatus().equals("RELEASE")?//release와 같다면 workflow는 complete
                                "Complete" : "In Progress",
                        c.getRevisedCnt() + 65,
                        c.getPresent(),
                        c.getCreatedAt(),
                        RouteProductDto.toProductDtoList(
                                routeProductRepository.findAllByRouteOrdering(
                                        routeOrderingRepository.findById(c.getId()).orElseThrow()
                                ),defaultImageAddress

                        ),

                        tmpRouteRejectPossibleResponse


//                        routeProductRepository.findAllByRouteOrdering(
//                                routeOrderingRepository.findById(c.getId()).orElseThrow()


                )
        ).collect(
                toList()
        );
        return newRouteDtos;
    }

    public static RouteOrderingDto toDto(
            RouteOrdering Route,
            RouteProductRepository routeProductRepository,
            RouteOrderingRepository routeOrderingRepository,
            RouteRejectPossibleResponse routeRejectPossibleResponse,
            String defaultImageAddress
    ) {

        return new RouteOrderingDto(
                Route.getId(),
                Route.getType(),
                Route.getLifecycleStatus(),
                Route.getLifecycleStatus().equals("COMPLETE")?
                        "COMPLETE":"WORKING",
                Route.getRevisedCnt(),
                Route.getPresent(),
                Route.getCreatedAt(),

                RouteProductDto.toProductDtoList(
                        routeProductRepository.findAllByRouteOrdering(
                                routeOrderingRepository.findById(
                                                Route.getId()
                                        )
                                        .orElseThrow()
                        ),
                        defaultImageAddress
                ),

                routeRejectPossibleResponse

        );
    }

}

package server.thn.Route.dto.routeProduct;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import server.thn.Member.dto.MemberDto;
import server.thn.Route.dto.routeOrdering.SeqAndName;
import server.thn.Route.entity.RouteProduct;
import server.thn.Route.entity.RouteProductMember;

import java.util.List;

import static java.util.stream.Collectors.toList;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RouteProductDto {

    private Long id;
    private Integer sequence;
    private String type;
    private String name;
    private String comment;
    private boolean passed;
    private boolean rejected;
    private SeqAndName refusal;
    private boolean show;
    private List<MemberDto> member;

    public static List<RouteProductDto> toProductDtoList(
            List <RouteProduct> RouteProducts,
            String defaultImageAddress
    ) {

        List<RouteProductDto> routeProductList = RouteProducts.stream().map(
                c -> new RouteProductDto(
                        c.getId(),
                        c.getSequence(),
                        c.getType().getName(),
                        c.getRoute_name(),
                        c.getComments(),
                        c.isPassed(),
                        c.isRejected(),
                        c.getRefusal()!=-1?new SeqAndName((RouteProducts.get(c.getRefusal()).getId().intValue()),
                                RouteProducts.get(c.getRefusal()).getRoute_name())
                                :                new SeqAndName(-1, "no refusal"),
                        c.isRoute_show(),
                        MemberDto.toDtoList(
                                c.getMembers().stream().map(
                                        RouteProductMember::getMember
                                ).collect(toList()),
                                defaultImageAddress
                        )
                )
        ).collect(
                toList()
        );
        return routeProductList;
    }

    public static RouteProductDto toDto(RouteProduct routeProduct,
                                        String defaultImageAddress) {

        return new RouteProductDto(
                routeProduct.getId(),
                routeProduct.getSequence(),
                routeProduct.getType().getName(),
                routeProduct.getRoute_name(),
                routeProduct.getComments(),
                routeProduct.isPassed(),
                routeProduct.isRejected(),
                new SeqAndName(-1, "no refusal"),
                routeProduct.isRoute_show(),
                MemberDto.toDtoList(
                        routeProduct.getMembers().stream().map(
                                RouteProductMember::getMember
                        ).collect(toList()),
                        defaultImageAddress

                )
        );
    }


}


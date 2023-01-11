package server.thn.Project.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;
import server.thn.Project.entity.buyer.buyerOrgClassification.BuyerOrganizationClassification2;
import server.thn.Project.entity.producer.produceOrgClassification.ProduceOrganizationClassification2;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class C2SelectDto {

    private Long id;
    private String name;
    private Integer last;
    private String value;
    private String classification;

    @Nullable
    List<C2SelectDto> c2SelectDtos;

    @Nullable
    private String api;

    public static List<C2SelectDto> toProduceOrgDtoList(
            String beforeName,
            String beforeId,
            List<ProduceOrganizationClassification2> classification2List
    ) {

        List<C2SelectDto> classification1SelectDtoList
                = classification2List.stream().map(

                c -> new C2SelectDto(
                        c.getId(),
                        c.getName(),
                        c.getLast(),

                        beforeName + "/" + c.getName(),
                        beforeId + "/" + c.getId(),

                        new ArrayList<>(),

                        " "
                )
        ).collect(
                toList()
        );

        return classification1SelectDtoList;
    }


    public static List<C2SelectDto> toBuyerOrgDtoList(
            String beforeName,
            String beforeId,
            List<BuyerOrganizationClassification2> classification2List
    ) {

        List<C2SelectDto> classification1SelectDtoList
                = classification2List.stream().map(

                c -> new C2SelectDto(
                        c.getId(),
                        c.getName(),
                        c.getLast(),

                        beforeName + "/" + c.getName(),
                        beforeId + "/" + c.getId(),

                        new ArrayList<>(),

                        " "
                )
        ).collect(
                toList()
        );

        return classification1SelectDtoList;
    }

}
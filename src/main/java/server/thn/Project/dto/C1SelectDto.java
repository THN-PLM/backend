package server.thn.Project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import server.thn.Project.entity.buyer.buyerOrgClassification.BuyerOrganizationClassification1;
import server.thn.Project.entity.producer.produceOrgClassification.ProduceOrganizationClassification1;
import server.thn.Project.repository.classification.BuyerOrganizationClassification2Repository;
import server.thn.Project.repository.classification.ProduceOrganizationClassification2Repository;

import java.util.List;
import static java.util.stream.Collectors.toList;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class C1SelectDto {
    private Long id;
    private String name;
    private Integer last;

    List<C2SelectDto> c2SelectDtos;
    private String value;

    public static C1SelectDto toDto(
            ProduceOrganizationClassification1 classification1,
            ProduceOrganizationClassification2Repository classification2Repository) {

        return new C1SelectDto(
                classification1.getId(),
                classification1.getName(),
                classification1.getLast(),
                C2SelectDto.toProduceOrgDtoList(
                        classification1.getId().toString(),
                        classification1.getName(),
                        classification2Repository.findByProduceOrganizationClassification1(classification1)
                ),
                classification1.getName()
        );
    }


    public static List<C1SelectDto> toProduceOrgDtoList(
            List<ProduceOrganizationClassification1> classification1List,
            ProduceOrganizationClassification2Repository classification2Repository
    ) {

        List<C1SelectDto> c1SelectDtoList
                = classification1List.stream().map(
                c -> new C1SelectDto(
                        c.getId(),
                        c.getName(),
                        c.getLast(),
                        C2SelectDto.toProduceOrgDtoList(
                                c.getName(),
                                c.getId().toString(),
                                classification2Repository.findByProduceOrganizationClassification1(c)
                        ),
                        c.getName()

                )
        ).collect(
                toList()
        );
        return c1SelectDtoList;
    }


    public static List<C1SelectDto> toBuyerOrgDtoList(
            List<BuyerOrganizationClassification1> classification1List,
            BuyerOrganizationClassification2Repository classification2Repository
    ) {

        List<C1SelectDto> c1SelectDtoList
                = classification1List.stream().map(
                c -> new C1SelectDto(
                        c.getId(),
                        c.getName(),
                        c.getLast(),
                        C2SelectDto.toBuyerOrgDtoList(
                                c.getName(),
                                c.getId().toString(),
                                classification2Repository.findByTeamClassification1(c)
                        ),
                        c.getName()

                )
        ).collect(
                toList()
        );
        return c1SelectDtoList;
    }

}

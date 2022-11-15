package server.thn.Project.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;
import server.thn.Project.entity.produceOrgClassification.ProduceOrganizationClassification1;
import server.thn.Project.entity.produceOrgClassification.ProduceOrganizationClassification2;
import server.thn.Project.repository.classification.ProduceOrganizationClassification1Repository;
import server.thn.Project.repository.classification.ProduceOrganizationClassification2Repository;

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

    public static List<C2SelectDto> toDtoList(
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
                        beforeId + "/" + c.getId() + "/" + (c.getLast() == 1 ? "99999" : ""),

                        new ArrayList<>(),

                        " "
                )
        ).collect(
                toList()
        );

        return classification1SelectDtoList;
    }

}
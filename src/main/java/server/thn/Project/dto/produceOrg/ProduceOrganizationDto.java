package server.thn.Project.dto.produceOrg;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import server.thn.Project.entity.buyer.BuyerOrganization;
import server.thn.Project.entity.producer.ProduceOrganization;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProduceOrganizationDto {

    private Long id;
    private String code1;
    private String code2;
    private String history1;
    private String history2;

    public static ProduceOrganizationDto toDto(ProduceOrganization produceOrganization){

        return new ProduceOrganizationDto(
                produceOrganization.getId(),
                produceOrganization.getCode1(),
                produceOrganization.getCode2(),
                produceOrganization.getDepartment().getProduceOrganizationClassification1().getName(),
                produceOrganization.getDepartment().getProduceOrganizationClassification2().getName()
        );
    }

    public static ProduceOrganizationDto toDto(BuyerOrganization produceOrganization){

        return new ProduceOrganizationDto(
                produceOrganization.getId(),
                produceOrganization.getCode1(),
                produceOrganization.getCode2(),
                produceOrganization.getDepartment().getClassification1().getName(),
                produceOrganization.getDepartment().getClassification2().getName()
        );
    }
}

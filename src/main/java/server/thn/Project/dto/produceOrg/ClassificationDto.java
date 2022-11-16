package server.thn.Project.dto.produceOrg;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import server.thn.Project.entity.produceOrgClassification.ProduceOrganizationClassification;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClassificationDto {

    private String value;
    private String classification;

    public static ClassificationDto toDto() {
            return new ClassificationDto();
    }

    public static ClassificationDto toDto(ProduceOrganizationClassification classification) {

        return new ClassificationDto(

                    classification.getProduceOrganizationClassification1().getName()
                            +"/"+classification.getProduceOrganizationClassification2().getName()
                            ,
                classification.getProduceOrganizationClassification1().getId()
                            +"/"+classification.getProduceOrganizationClassification2().getId()

            );
        }
    }

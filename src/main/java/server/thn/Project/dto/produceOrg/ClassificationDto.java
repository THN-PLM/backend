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

                    classification.getClassification1().getName()
                            +"/"+classification.getClassification2().getName()
                            ,
                    classification.getClassification1().getId()
                            +"/"+classification.getClassification2().getId()

            );
        }
    }

package server.thn.Project.entity.produceOrgClassification;

import lombok.*;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@EqualsAndHashCode
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ProduceOrganizationClassificationId implements Serializable {

    private ProduceOrganizationClassification1 classification1;

    private ProduceOrganizationClassification2 classification2;
    
}

package server.thn.Project.entity.buyer.buyerOrgClassification;

import lombok.*;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@EqualsAndHashCode
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class BuyerOrganizationClassificationId implements Serializable {

    private BuyerOrganizationClassification1 classification1;

    private BuyerOrganizationClassification2 classification2;

}

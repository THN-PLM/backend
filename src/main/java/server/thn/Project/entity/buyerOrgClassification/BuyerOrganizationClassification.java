package server.thn.Project.entity.buyerOrgClassification;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@IdClass(BuyerOrganizationClassificationId.class)
public class BuyerOrganizationClassification {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teamClassification1_id")
    private BuyerOrganizationClassification1 classification1;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teamClassification2_id")
    private BuyerOrganizationClassification2 classification2;

    public BuyerOrganizationClassification(BuyerOrganizationClassification1 classification1, BuyerOrganizationClassification2 classification2) {
        this.classification1 = classification1;
        this.classification2 = classification2;
    }

}

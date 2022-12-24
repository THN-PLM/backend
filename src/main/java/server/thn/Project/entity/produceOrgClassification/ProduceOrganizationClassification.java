package server.thn.Project.entity.produceOrgClassification;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@IdClass(ProduceOrganizationClassificationId.class)
public class ProduceOrganizationClassification {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produceOrganizationClassification1_id")
    private ProduceOrganizationClassification1 produceOrganizationClassification1;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produceOrganizationClassification2_id")
    private ProduceOrganizationClassification2 produceOrganizationClassification2;

    public ProduceOrganizationClassification(ProduceOrganizationClassification1 classification1, ProduceOrganizationClassification2 classification2) {

        this.produceOrganizationClassification1 = classification1;
        this.produceOrganizationClassification2 = classification2;

    }

}

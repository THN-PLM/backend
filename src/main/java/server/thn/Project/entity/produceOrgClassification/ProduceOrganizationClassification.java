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
    @JoinColumn(name = "teamClassification1_id")
    private ProduceOrganizationClassification1 classification1;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teamClassification2_id")
    private ProduceOrganizationClassification2 classification2;

    public ProduceOrganizationClassification(ProduceOrganizationClassification1 classification1, ProduceOrganizationClassification2 classification2) {
        this.classification1 = classification1;
        this.classification2 = classification2;
    }

}

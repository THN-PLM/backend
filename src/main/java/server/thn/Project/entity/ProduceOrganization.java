package server.thn.Project.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import server.thn.Project.entity.produceOrgClassification.ProduceOrganizationClassification;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProduceOrganization {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE2")
    @SequenceGenerator(name="SEQUENCE2", sequenceName="SEQUENCE2", allocationSize=1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "t1_id", nullable = false)
    @JoinColumn(name = "t2_id", nullable = false)
    private ProduceOrganizationClassification department;

    @Column(nullable = false)
    private String code1;

    @Column(nullable = false)
    private String code2;

    @Column
    private boolean notDeleted;

    public void setNotDeleted(boolean notDeleted) {
        this.notDeleted = notDeleted;
    }

    public ProduceOrganization(
            String code1,
            String code2,
            ProduceOrganizationClassification produceOrganizationClassification
    ){
        this.code1 = code1;
        this.code2 = code2;
        this.department = produceOrganizationClassification;
        this.notDeleted = true;
    }
}

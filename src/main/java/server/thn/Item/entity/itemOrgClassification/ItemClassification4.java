package server.thn.Item.entity.itemOrgClassification;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemClassification4 {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE2")
    @SequenceGenerator(name="SEQUENCE2", sequenceName="SEQUENCE2", allocationSize=1)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private Integer last;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teamClassification3_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ItemClassification3 teamClassification3;

}



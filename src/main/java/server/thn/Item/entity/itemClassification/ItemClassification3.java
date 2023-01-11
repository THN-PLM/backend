package server.thn.Item.entity.itemClassification;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemClassification3 {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE2")
    @SequenceGenerator(name="SEQUENCE2", sequenceName="SEQUENCE2", allocationSize=1)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private Integer last;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teamClassification2_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ItemClassification2 teamClassification2;

    @OneToMany(
            mappedBy = "teamClassification3",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<ItemClassification4> teamClassification4_list;

}

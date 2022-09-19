package server.thn.Member.entity.teamClassification;

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
public class TeamClassification2{
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE2")
    @SequenceGenerator(name="SEQUENCE2", sequenceName="SEQUENCE2", allocationSize=1)
    private Long id;


    @Column(nullable = false)
    private String name;

    @Column
    private Integer last;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teamClassification1_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private TeamClassification1 teamClassification1;

    @OneToMany(
            mappedBy = "teamClassification2",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<TeamClassification3> teamClassification3_list;
}


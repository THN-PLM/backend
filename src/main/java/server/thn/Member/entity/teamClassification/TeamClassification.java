package server.thn.Member.entity.teamClassification;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@IdClass(TeamClassificationId.class)
public class TeamClassification {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teamClassification1_id")
    private TeamClassification1 classification1;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teamClassification2_id")
    private TeamClassification2 classification2;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teamClassification3_id")
    private TeamClassification3 classification3;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teamClassification4_id")
    private TeamClassification4 classification4;

    public TeamClassification(TeamClassification1 classification1, TeamClassification2 classification2, TeamClassification3 classification3, TeamClassification4 classification4) {
        this.classification1 = classification1;
        this.classification2 = classification2;
        this.classification3 = classification3;
        this.classification4 = classification4;
    }

}

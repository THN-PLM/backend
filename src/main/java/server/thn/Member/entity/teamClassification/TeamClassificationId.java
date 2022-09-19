package server.thn.Member.entity.teamClassification;

import lombok.*;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@EqualsAndHashCode
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class TeamClassificationId implements Serializable {

    private TeamClassification1 classification1;

    private TeamClassification2 classification2;

    private TeamClassification3 classification3;

    private TeamClassification4 classification4;
}

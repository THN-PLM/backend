package server.thn.Member.repository.classification;

import org.springframework.data.jpa.repository.JpaRepository;
import server.thn.Member.entity.teamClassification.TeamClassification2;
import server.thn.Member.entity.teamClassification.TeamClassification3;

import java.util.List;

public interface TeamClassification3Repository  extends JpaRepository<TeamClassification3, Long> {
    List<TeamClassification3> findByTeamClassification2(TeamClassification2 teamClassification2);
}

package server.thn.Member.repository.classification;

import org.springframework.data.jpa.repository.JpaRepository;
import server.thn.Member.entity.teamClassification.TeamClassification3;
import server.thn.Member.entity.teamClassification.TeamClassification4;

import java.util.List;

public interface TeamClassification4Repository extends JpaRepository<TeamClassification4, Long> {
    List<TeamClassification4> findByTeamClassification3(TeamClassification3 teamClassification3);
}

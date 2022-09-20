package server.thn.Member.repository.classification;

import org.springframework.data.jpa.repository.JpaRepository;
import server.thn.Member.entity.teamClassification.TeamClassification1;
import server.thn.Member.entity.teamClassification.TeamClassification2;

import java.util.List;

public interface TeamClassification2Repository extends JpaRepository<TeamClassification2, Long> {
    List<TeamClassification2> findByTeamClassification1(TeamClassification1 classification1);

}

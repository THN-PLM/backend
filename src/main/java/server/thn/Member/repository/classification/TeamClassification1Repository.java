package server.thn.Member.repository.classification;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import server.thn.Member.entity.teamClassification.TeamClassification1;

import java.util.List;

public interface TeamClassification1Repository extends JpaRepository<TeamClassification1, Long> {
    @Query("select c from TeamClassification1 c join fetch c.teamClassification2List ")
    List<TeamClassification1> findAllByTeamClassification1();

}
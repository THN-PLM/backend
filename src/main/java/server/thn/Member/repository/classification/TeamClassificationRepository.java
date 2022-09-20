package server.thn.Member.repository.classification;

import org.springframework.data.jpa.repository.JpaRepository;
import server.thn.Member.entity.teamClassification.*;

public interface TeamClassificationRepository extends JpaRepository<TeamClassification, Long> {

    TeamClassification
    findByClassification1AndClassification2AndClassification3AndClassification4(
            TeamClassification1 classification1,
            TeamClassification2 classification2,
            TeamClassification3 classification3,
            TeamClassification4 classification4
    );

}

package server.thn.Route.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import server.thn.Project.entity.Project;
import server.thn.Route.entity.RouteOrdering;

import java.util.List;

public interface RouteOrderingRepository extends JpaRepository<RouteOrdering, Long> {

    List<RouteOrdering> findByProjectOrderByIdAsc(Project project);

    RouteOrdering findByRevisedCnt(Integer integer);

}

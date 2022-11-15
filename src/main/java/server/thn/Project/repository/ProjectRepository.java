package server.thn.Project.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import server.thn.Member.entity.Member;
import server.thn.Project.entity.Project;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    Page<Project> findAll(Pageable pageable);

    Page<Project> findByMember(Member member, Pageable pageable);

    List<Project> findByMember(Member member);

    List<Project> findByNameContainingIgnoreCaseOrProjectNumberContainingIgnoreCaseOrLifecycleContainingIgnoreCase
            (String name,
             String projectNumber,
             String lifecycle
            );

    @Query(
            "select i from Project " +
                    "i where i IN (:projects)"
    )
    Page<Project> findByProjects(@Param("projects") List<Project> projects, Pageable pageable);


}
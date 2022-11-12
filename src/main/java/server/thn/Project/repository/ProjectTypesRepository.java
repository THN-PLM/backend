package server.thn.Project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import server.thn.Project.entity.ProjectType;
import server.thn.Project.entity.ProjectTypeEnum;

public interface ProjectTypesRepository extends JpaRepository<ProjectType, Long> {

    ProjectType findByProjectTypeEnum(ProjectTypeEnum projectTypeEnum);
}

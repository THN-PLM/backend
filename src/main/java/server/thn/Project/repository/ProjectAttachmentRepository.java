package server.thn.Project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import server.thn.Project.entity.Project;
import server.thn.Project.entity.ProjectAttachment;

import java.util.List;

public interface ProjectAttachmentRepository extends JpaRepository<ProjectAttachment, Long> {

    List<ProjectAttachment> findByProject(Project project);

}


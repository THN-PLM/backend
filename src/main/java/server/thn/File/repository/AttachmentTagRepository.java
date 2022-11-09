package server.thn.File.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import server.thn.File.entity.AttachmentTag;

public interface AttachmentTagRepository extends JpaRepository<AttachmentTag, Long>{
}

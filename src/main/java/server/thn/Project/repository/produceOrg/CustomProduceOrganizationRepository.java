package server.thn.Project.repository.produceOrg;

import org.springframework.data.domain.Page;
import server.thn.Project.dto.ReadCondition;
import server.thn.Project.dto.ReadResponse;

public interface CustomProduceOrganizationRepository {
    Page<ReadResponse > findAllByCondition(ReadCondition cond);
}
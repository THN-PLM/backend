package server.thn.Project.repository.buyer;

import org.springframework.data.domain.Page;
import server.thn.Project.dto.ReadCondition;
import server.thn.Project.dto.ReadResponse;

public interface CustomBuyerOrganizationRepository {
    Page<ReadResponse> findAllByCondition(ReadCondition cond);
}



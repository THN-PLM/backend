package server.thn.Member.repository;

import org.springframework.data.domain.Page;
import server.thn.Member.dto.MemberReadCondition;
import server.thn.Member.dto.MemberSimpleDto;

public interface CustomMemberRepository {
   Page<MemberSimpleDto> findAllByCondition(MemberReadCondition cond);
}
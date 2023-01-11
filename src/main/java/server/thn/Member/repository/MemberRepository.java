package server.thn.Member.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import server.thn.Member.entity.Member;
import server.thn.Project.entity.producer.produceOrgClassification.ProduceOrganizationClassification;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, CustomMemberRepository{

    Optional<Member> findByEmail(String email);
    Optional<Member> findByUsername(String username);

    //Optional<Member> findById(Long id);//member_id 라고 되어있어서 이런 식으로 명명함

    boolean existsByEmail(String email);

    List<Member> findByUsernameContainingIgnoreCase(String username);
    Page<Member> findByUsernameContainingIgnoreCase(String username, Pageable pageable);


    @Query(
            "select i from Member " +
                    "i where i IN (:members)"
    )
    Page<Member> findByMembers(@Param("members") List<Member> members, Pageable pageable);

    List<Member> findByDepartment(ProduceOrganizationClassification teamClassification);
    Page<Member> findByDepartment(ProduceOrganizationClassification teamClassification, Pageable pageable);

}

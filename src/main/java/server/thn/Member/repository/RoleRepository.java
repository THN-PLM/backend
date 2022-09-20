package server.thn.Member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import server.thn.Member.entity.Role;
import server.thn.Member.entity.RoleType;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleType(RoleType roleType);
}

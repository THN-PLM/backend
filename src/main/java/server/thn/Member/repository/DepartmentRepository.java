package server.thn.Member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import server.thn.Member.entity.Department;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
}
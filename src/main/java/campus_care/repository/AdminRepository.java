package campus_care.repository;

import campus_care.entity.Admin;
import campus_care.enums.AdminCategory;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {

    Optional<Admin> findByEmail(String email);

    Optional<Admin> findByCategory(AdminCategory category);
}
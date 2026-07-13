package campus_care.repository;

import campus_care.entity.AuthorizedStudent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorizedStudentRepository
        extends JpaRepository<AuthorizedStudent, String> {

    Optional<AuthorizedStudent> findByCollegeId(String collegeId);
}
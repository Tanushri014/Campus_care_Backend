package campus_care.repository;
import campus_care.entity.PendingStudent;

import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PendingStudentRepository
        extends JpaRepository<PendingStudent, Long> {

    Optional<PendingStudent> findByStudentEmail(String studentEmail);

    boolean existsByStudentEmail(String studentEmail);

    boolean existsByCollegeId(String collegeId);


    void deleteByCreatedAtBefore(LocalDateTime dateTime);
}

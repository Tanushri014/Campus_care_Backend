package campus_care.repository;

import campus_care.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student,Long> {
    Optional<Student> findByStudentEmail(String studentEmail);



    Optional<Student> findByCollegeId(String collegeId);
}


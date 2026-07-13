package campus_care.repository;

import campus_care.entity.EmailOtp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface OtpRepository
        extends JpaRepository<EmailOtp, Long> {
    void deleteByExpiryTimeBefore(LocalDateTime expiryTime);
    Optional<EmailOtp> findByStudentEmail(String studentEmail);
    void deleteByStudentEmail(String studentEmail);
}
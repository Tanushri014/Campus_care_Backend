package campus_care.repository;

import campus_care.entity.Complaint;
import campus_care.enums.ComplaintCategory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComplaintRepository
        extends JpaRepository<Complaint, Long> {

    List<Complaint> findByComplaintCategory(
            ComplaintCategory category
    );

    // For Admin pagination
    Page<Complaint> findByComplaintCategory(
            ComplaintCategory category,
            Pageable pageable
    );

    // For Student pagination
    Page<Complaint> findByStudentId(
            Long studentId,
            Pageable pageable
    );
}
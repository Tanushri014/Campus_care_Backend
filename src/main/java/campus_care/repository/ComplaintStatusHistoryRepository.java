package campus_care.repository;

import campus_care.entity.ComplaintStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ComplaintStatusHistoryRepository
        extends JpaRepository<ComplaintStatusHistory, Long> {

    List<ComplaintStatusHistory> findByComplaintId(Long complaintId);
}

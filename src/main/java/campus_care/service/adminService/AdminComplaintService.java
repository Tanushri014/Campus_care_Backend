package campus_care.service.adminService;

import campus_care.entity.Complaint;
import campus_care.entity.Feedback;

import org.springframework.data.domain.Page;

public interface AdminComplaintService {

    Page<Complaint> getComplaintsForAdmin(
            String adminEmail,
            int page,
            int size
    );

    Complaint getComplaintById(
            Long complaintId,
            String adminEmail
    );

    Complaint updateComplaintStatus(
            Long complaintId,
            String status,
            String message,
            String adminEmail
    );

    Feedback getFeedbackByComplaintId(
            Long complaintId,
            String adminEmail
    );
}
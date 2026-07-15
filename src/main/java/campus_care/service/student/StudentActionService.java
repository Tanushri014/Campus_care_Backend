package campus_care.service.student;

import campus_care.dto.ComplaintResponseDto;
import campus_care.entity.Complaint;
import campus_care.entity.ComplaintStatusHistory;
import campus_care.entity.Feedback;
import campus_care.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StudentActionService {

    Student getCurrentStudent(
            String studentEmail
    );

    ComplaintResponseDto createComplaint(
            String studentEmail,
            Complaint complaint,
            MultipartFile imageFile
    );

    Page<ComplaintResponseDto> getMyComplaints(
            String studentEmail,
            int page,
            int size
    );

    ComplaintResponseDto getComplaintByIdForStudent(
            Long complaintId,
            String studentEmail
    );

    ComplaintResponseDto updateComplaint(
            Long complaintId,
            Complaint complaint,
            String studentEmail
    );

    void deleteComplaint(
            Long complaintId,
            String studentEmail
    );

    Feedback giveFeedback(
            Long complaintId,
            Feedback feedback,
            String studentEmail
    );

    List<ComplaintStatusHistory> getComplaintTracking(
            Long complaintId,
            String studentEmail
    );
}
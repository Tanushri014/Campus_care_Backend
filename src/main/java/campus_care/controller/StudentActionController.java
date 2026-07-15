package campus_care.controller;

import campus_care.dto.ComplaintResponseDto;
import campus_care.dto.FeedbackDto;
import campus_care.entity.Complaint;
import campus_care.entity.ComplaintStatusHistory;
import campus_care.entity.Feedback;
import campus_care.entity.Student;
import campus_care.enums.ComplaintCategory;
import campus_care.enums.ComplaintStatus;
import campus_care.service.student.StudentActionService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/student")
@AllArgsConstructor
public class StudentActionController {

    private final StudentActionService studentActionService;

    /* =========================================
       CURRENT STUDENT
    ========================================= */

    @GetMapping("/me")
    public ResponseEntity<Student> getCurrentStudent(
            Authentication authentication
    ) {

        Student student =
                studentActionService.getCurrentStudent(
                        authentication.getName()
                );

        return ResponseEntity.ok(student);
    }

    /* =========================================
       CREATE COMPLAINT
    ========================================= */

    @PostMapping(
            value = "/complaints",
            consumes = "multipart/form-data"
    )
    public ResponseEntity<ComplaintResponseDto> createComplaint(

            Authentication authentication,

            @RequestParam("title")
            String title,

            @RequestParam("description")
            String description,

            @RequestParam("complaintCategory")
            ComplaintCategory complaintCategory,

            @RequestParam(
                    value = "image",
                    required = false
            )
            MultipartFile imageFile
    ) {

        Complaint complaint = new Complaint();

        complaint.setTitle(title);
        complaint.setDescription(description);
        complaint.setComplaintCategory(complaintCategory);
        complaint.setStatus(ComplaintStatus.PENDING);

        ComplaintResponseDto savedComplaint =
                studentActionService.createComplaint(
                        authentication.getName(),
                        complaint,
                        imageFile
                );

        return new ResponseEntity<>(
                savedComplaint,
                HttpStatus.CREATED
        );
    }

    /* =========================================
       MY COMPLAINTS
    ========================================= */

    @GetMapping("/complaints")
    public ResponseEntity<Page<ComplaintResponseDto>> getMyComplaints(

            Authentication authentication,

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size
    ) {

        return ResponseEntity.ok(
                studentActionService.getMyComplaints(
                        authentication.getName(),
                        page,
                        size
                )
        );
    }

    /* =========================================
       SINGLE COMPLAINT
    ========================================= */

    @GetMapping("/complaints/{id}")
    public ResponseEntity<ComplaintResponseDto> getComplaintById(

            @PathVariable Long id,

            Authentication authentication
    ) {

        return ResponseEntity.ok(
                studentActionService.getComplaintByIdForStudent(
                        id,
                        authentication.getName()
                )
        );
    }

    /* =========================================
       UPDATE COMPLAINT
    ========================================= */

    @PutMapping("/complaints/{id}")
    public ResponseEntity<ComplaintResponseDto> updateComplaint(

            @PathVariable Long id,

            @RequestBody Complaint complaint,

            Authentication authentication
    ) {

        return ResponseEntity.ok(
                studentActionService.updateComplaint(
                        id,
                        complaint,
                        authentication.getName()
                )
        );
    }

    /* =========================================
       DELETE COMPLAINT
    ========================================= */

    @DeleteMapping("/complaints/{id}")
    public ResponseEntity<String> deleteComplaint(

            @PathVariable Long id,

            Authentication authentication
    ) {

        studentActionService.deleteComplaint(
                id,
                authentication.getName()
        );

        return ResponseEntity.ok(
                "Complaint deleted successfully"
        );
    }

    /* =========================================
       FEEDBACK
    ========================================= */

    @PostMapping("/complaints/{id}/feedback")
    public ResponseEntity<Feedback> submitFeedback(

            @PathVariable Long id,

            @RequestBody FeedbackDto dto,

            Authentication authentication
    ) {

        Feedback feedback = new Feedback();

        feedback.setMessage(dto.getMessage());
        feedback.setRating(dto.getRating());

        Feedback savedFeedback =
                studentActionService.giveFeedback(
                        id,
                        feedback,
                        authentication.getName()
                );

        return ResponseEntity.ok(savedFeedback);
    }

    /* =========================================
       COMPLAINT HISTORY
    ========================================= */

    @GetMapping("/complaints/{id}/history")
    public ResponseEntity<List<ComplaintStatusHistory>> getComplaintHistory(

            @PathVariable Long id,

            Authentication authentication
    ) {

        return ResponseEntity.ok(
                studentActionService.getComplaintTracking(
                        id,
                        authentication.getName()
                )
        );
    }
}
package campus_care.controller;

import campus_care.dto.AdminProfileDto;
import campus_care.dto.ComplaintStatusDto;
import campus_care.entity.Complaint;
import campus_care.entity.Feedback;
import campus_care.service.adminService.AdminComplaintService;
import campus_care.service.adminService.AdminService;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    private final AdminComplaintService adminComplaintService;

    /* =========================================
       CURRENT ADMIN
    ========================================= */

    @GetMapping("/me")
    public ResponseEntity<AdminProfileDto> getCurrentAdmin(
            Authentication authentication
    ) {

        return ResponseEntity.ok(
                adminService.getCurrentAdmin(
                        authentication.getName()
                )
        );
    }

    /* =========================================
       ADMIN COMPLAINTS
    ========================================= */

    @GetMapping("/complaints")
    public ResponseEntity<Page<Complaint>> getComplaintsForAdmin(

            Authentication authentication,

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size
    ) {

        return ResponseEntity.ok(
                adminComplaintService.getComplaintsForAdmin(
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
    public ResponseEntity<Complaint> getComplaintById(

            @PathVariable Long id,

            Authentication authentication
    ) {

        return ResponseEntity.ok(
                adminComplaintService.getComplaintById(
                        id,
                        authentication.getName()
                )
        );
    }

    /* =========================================
       UPDATE STATUS
    ========================================= */

    @PutMapping("/complaints/{id}/status")
    public ResponseEntity<Complaint> updateComplaintStatus(

            @PathVariable Long id,

            @RequestBody ComplaintStatusDto dto,

            Authentication authentication
    ) {

        return ResponseEntity.ok(
                adminComplaintService.updateComplaintStatus(
                        id,
                        dto.getStatus(),
                        dto.getMessage(),
                        authentication.getName()
                )
        );
    }

    /* =========================================
       FEEDBACK
    ========================================= */

    @GetMapping("/complaints/{id}/feedback")
    public ResponseEntity<Feedback> getFeedback(

            @PathVariable Long id,

            Authentication authentication
    ) {

        return ResponseEntity.ok(
                adminComplaintService.getFeedbackByComplaintId(
                        id,
                        authentication.getName()
                )
        );
    }
}
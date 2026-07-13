package campus_care.service.adminService;

import campus_care.entity.Admin;
import campus_care.entity.Complaint;
import campus_care.entity.Feedback;
import campus_care.exception.ResourceNotFoundException;
import campus_care.repository.AdminRepository;
import campus_care.repository.ComplaintRepository;
import campus_care.repository.FeedbackRepository;
import  campus_care.enums.AdminCategory;
import campus_care.enums.ComplaintCategory;
import lombok.RequiredArgsConstructor;
import campus_care.enums.ComplaintStatus;
import  campus_care.exception.UnauthorizedAcessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
@Service
@RequiredArgsConstructor
@Slf4j
public class AdminComplaintServiceImpl
        implements AdminComplaintService {

    private final ComplaintRepository complaintRepository;

    private final AdminRepository adminRepository;

    private final FeedbackRepository feedbackRepository;

    @Override
    public Page<Complaint> getComplaintsForAdmin(
            String adminEmail,
            int page,
            int size
    ) {

        log.info("Fetching complaints for admin: {}", adminEmail);
        Admin admin = getAdminByEmail(adminEmail);
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("createdAt").descending()
        );
        if (admin.getCategory() == AdminCategory.MAIN) {
            log.info("MAIN admin '{}' requested all complaints.", adminEmail);
            return complaintRepository.findAll(pageable);
        }

        ComplaintCategory category =
                ComplaintCategory.valueOf(
                        admin.getCategory().name()
                );
        log.info(
                "Admin '{}' requested complaints for category '{}'.",
                adminEmail,
                category);

        return complaintRepository.findByComplaintCategory(category,pageable);
    }

    @Override
    public Complaint getComplaintById(
            Long complaintId,
            String adminEmail
    ) {
        log.info(
                "Admin '{}' requested complaint with ID {}.",
                adminEmail,
                complaintId
        );
        Admin admin = getAdminByEmail(adminEmail);

        Complaint complaint = getComplaint(complaintId);

        validateAdminAccess(admin, complaint);

        return complaint;
    }

    @Override
    public Complaint updateComplaintStatus(
            Long complaintId,
            String status,
            String message,
            String adminEmail
    ) {
        log.info(
                "Admin '{}' updating complaint {} to status '{}'.",
                adminEmail,
                complaintId,
                status
        );
        Complaint complaint =
                getComplaintById(complaintId, adminEmail);

        complaint.setStatus(
                ComplaintStatus.valueOf(status)
        );

        complaint.setAdminMessage(message);
        log.info(
                "Complaint {} updated successfully by admin '{}'.",
                complaintId,
                adminEmail
        );

        return complaintRepository.save(complaint);
    }

    @Override
    public Feedback getFeedbackByComplaintId(
            Long complaintId,
            String adminEmail
    ) {
        log.info(
                "Admin '{}' requested feedback for complaint {}.",
                adminEmail,
                complaintId
        );
        getComplaintById(complaintId, adminEmail);

        return feedbackRepository
                .findByComplaintId(complaintId)
                .orElseThrow(() ->{
                    log.warn(
                            "Feedback not found for complaint {}.",
                            complaintId
                    );

                       return  new ResourceNotFoundException(
                                "Feedback not found"
                        );

                });
    }

    /* =========================================
       PRIVATE HELPERS
    ========================================= */

    private Admin getAdminByEmail(
            String email
    ) {

        return adminRepository.findByEmail(email)
                .orElseThrow(() ->{

                    log.warn(
                            "Admin not found with email '{}'.",
                            email
                    );

                   return  new ResourceNotFoundException(
                            "Admin not found"
                    );

                });


    }

    private Complaint getComplaint(
            Long complaintId
    ) {

        return complaintRepository.findById(complaintId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Complaint not found"
                        ));
    }

    private void validateAdminAccess(
            Admin admin,
            Complaint complaint
    ) {

        if (admin.getCategory() == AdminCategory.MAIN) {
            return;
        }

        ComplaintCategory category =
                ComplaintCategory.valueOf(
                        admin.getCategory().name()
                );

        if (complaint.getComplaintCategory() != category) {
            throw new UnauthorizedAcessException(
                    "You are not authorized to access this complaint."
            );
        }
    }
}
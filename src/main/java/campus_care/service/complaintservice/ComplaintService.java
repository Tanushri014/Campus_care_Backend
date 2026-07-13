package campus_care.service.complaintservice;

import campus_care.entity.Admin;
import campus_care.entity.Complaint;
import campus_care.enums.AdminCategory;
import campus_care.enums.ComplaintCategory;
import campus_care.exception.ResourceNotFoundException;
import campus_care.repository.AdminRepository;
import campus_care.repository.ComplaintRepository;
import campus_care.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ComplaintService {

    private static final String EMAIL_SUBJECT =
            "New Complaint Submitted";

    private final ComplaintRepository complaintRepository;

    private final AdminRepository adminRepository;

    private final EmailService emailService;

    /* =========================================
       CREATE COMPLAINT
    ========================================= */

    public Complaint createComplaint(
            Complaint complaint
    ) {

        Complaint savedComplaint =
                complaintRepository.save(
                        complaint
                );

        sendComplaintNotification(
                savedComplaint
        );

        return savedComplaint;
    }

    /* =========================================
       SEND EMAIL TO CONCERNED ADMIN
    ========================================= */

    private void sendComplaintNotification(
            Complaint complaint
    ) {

        try {

            Admin admin =
                    getAdminByCategory(
                            complaint.getComplaintCategory()
                    );

            if (admin.getEmail() == null) {
                return;
            }

            emailService.sendEmail(
                    admin.getEmail(),
                    EMAIL_SUBJECT,
                    buildEmailBody(complaint)
            );

        } catch (Exception exception) {

            log.error(
                    "Failed to send complaint notification",
                    exception
            );
        }
    }

    /* =========================================
       GET ADMIN BY CATEGORY
    ========================================= */

    private Admin getAdminByCategory(
            ComplaintCategory category
    ) {

        AdminCategory adminCategory =
                AdminCategory.valueOf(
                        category.name()
                );

        return adminRepository
                .findByCategory(adminCategory)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Admin not found for category: "
                                        + category
                        )
                );
    }

    /* =========================================
       BUILD EMAIL BODY
    ========================================= */

    private String buildEmailBody(
            Complaint complaint
    ) {

        return "A new complaint has been submitted.\n\n"
                + "Category: "
                + complaint.getComplaintCategory()
                + "\n"
                + "Title: "
                + complaint.getTitle()
                + "\n"
                + "Description: "
                + complaint.getDescription();
    }
}
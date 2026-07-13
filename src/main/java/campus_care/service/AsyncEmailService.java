package campus_care.service.announcement;

import campus_care.entity.Admin;
import campus_care.entity.Announcement;
import campus_care.entity.Student;
import campus_care.repository.AdminRepository;
import campus_care.repository.StudentRepository;
import campus_care.service.EmailService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AsyncEmailService {

    private static final Logger logger =
            LoggerFactory.getLogger(
                    AsyncEmailService.class
            );

    private final StudentRepository studentRepository;
    private final AdminRepository adminRepository;
    private final EmailService emailService;

    public AsyncEmailService(
            StudentRepository studentRepository,
            AdminRepository adminRepository,
            EmailService emailService
    ) {

        this.studentRepository = studentRepository;
        this.adminRepository = adminRepository;
        this.emailService = emailService;
    }

    @Async
    public void sendAnnouncementEmails(
            Long uploaderAdminId,
            Announcement announcement
    ) {

        String subject =
                "New College Announcement";

        List<Student> students =
                studentRepository.findAll();

        List<Admin> admins =
                adminRepository.findAll()
                        .stream()
                        .filter(admin ->
                                !admin.getId()
                                        .equals(
                                                uploaderAdminId
                                        )
                        )
                        .toList();

        sendEmailsToStudents(
                students,
                announcement,
                subject
        );

        sendEmailsToAdmins(
                admins,
                announcement,
                subject
        );
    }

    private void sendEmailsToStudents(
            List<Student> students,
            Announcement announcement,
            String subject
    ) {

        for (Student student : students) {

            try {

                emailService.sendEmail(
                        student.getStudentEmail(),
                        subject,
                        announcement.getDescription()
                );

            } catch (Exception e) {

                logger.error(
                        "Failed to send email to student: {}",
                        student.getStudentEmail(),
                        e
                );
            }
        }
    }

    private void sendEmailsToAdmins(
            List<Admin> admins,
            Announcement announcement,
            String subject
    ) {

        for (Admin admin : admins) {

            try {

                emailService.sendEmail(
                        admin.getEmail(),
                        subject,
                        announcement.getDescription()
                );

            } catch (Exception e) {

                logger.error(
                        "Failed to send email to admin: {}",
                        admin.getEmail(),
                        e
                );
            }
        }
    }
}
package campus_care.service.student;

import campus_care.dto.ComplaintResponseDto;
import campus_care.entity.Complaint;
import campus_care.entity.ComplaintStatusHistory;
import campus_care.entity.Feedback;
import campus_care.entity.Student;
import campus_care.enums.FileType;
import campus_care.exception.InvalidFileException;
import campus_care.exception.ResourceNotFoundException;
import campus_care.exception.UnauthorizedAcessException;
import campus_care.repository.ComplaintRepository;
import campus_care.repository.ComplaintStatusHistoryRepository;
import campus_care.repository.FeedbackRepository;
import campus_care.repository.StudentRepository;
import campus_care.service.complaintservice.ComplaintService;
import campus_care.service.storage.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentActionServiceImpl
        implements StudentActionService {

    private static final long MAX_IMAGE_SIZE =
            2 * 1024 * 1024;

    private final StudentRepository studentRepository;

    private final ComplaintRepository complaintRepository;

    private final ComplaintStatusHistoryRepository
            complaintStatusHistoryRepository;

    private final FeedbackRepository feedbackRepository;

    private final ComplaintService complaintService;

    private final FileStorageService fileStorageService;

    /* =========================================
       CURRENT STUDENT
    ========================================= */

    @Override
    public Student getCurrentStudent(
            String studentEmail
    ) {

        return getStudentByEmail(studentEmail);
    }

    /* =========================================
       CREATE COMPLAINT
    ========================================= */

    @Override
    public ComplaintResponseDto createComplaint(
            String studentEmail,
            Complaint complaint,
            MultipartFile imageFile
    ) {

        Student student = getStudentByEmail(studentEmail);

        validateImage(imageFile);

        if (imageFile != null && !imageFile.isEmpty()) {

            String imagePublicId = fileStorageService.store(
                    imageFile,
                    FileType.COMPLAINT
            );

            complaint.setImageUrl(imagePublicId);
        }

        complaint.setStudent(student);

        Complaint savedComplaint =
                complaintService.createComplaint(complaint);

        return mapToDto(savedComplaint);
    }
    /* =========================================
       MY COMPLAINTS
    ========================================= */

    @Override
    public Page<ComplaintResponseDto> getMyComplaints(
            String studentEmail,
            int page,
            int size
    ) {

        Student student = getStudentByEmail(studentEmail);

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("createdAt").descending()
        );

        return complaintRepository
                .findByStudentId(student.getId(), pageable)
                .map(this::mapToDto);
    }
    /* =========================================
       GET COMPLAINT
    ========================================= */

    @Override
    public ComplaintResponseDto getComplaintByIdForStudent(
            Long complaintId,
            String studentEmail
    ) {

        Student student = getStudentByEmail(studentEmail);

        Complaint complaint = complaintRepository.findById(complaintId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Complaint not found."
                        ));

        if (!complaint.getStudent().getId().equals(student.getId())) {
            throw new UnauthorizedAcessException(
                    "Unauthorized access."
            );
        }

        return mapToDto(complaint);
    }

    /* =========================================
       UPDATE COMPLAINT
    ========================================= */

    @Override
    public ComplaintResponseDto updateComplaint(
            Long complaintId,
            Complaint updatedComplaint,
            String studentEmail
    ) {

        Student student = getStudentByEmail(studentEmail);

        Complaint complaint = complaintRepository.findById(complaintId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Complaint not found."
                        ));

        if (!complaint.getStudent().getId().equals(student.getId())) {
            throw new UnauthorizedAcessException(
                    "Unauthorized access."
            );
        }

        complaint.setTitle(updatedComplaint.getTitle());
        complaint.setDescription(updatedComplaint.getDescription());
        complaint.setComplaintCategory(updatedComplaint.getComplaintCategory());

        Complaint savedComplaint = complaintRepository.save(complaint);

        return mapToDto(savedComplaint);
    }
    /* =========================================
       DELETE COMPLAINT
    ========================================= */

    @Override
    public void deleteComplaint(
            Long complaintId,
            String studentEmail
    ) {

        Complaint complaint =
                getComplaintByIdForStudent(
                        complaintId,
                        studentEmail
                );

        if (complaint.getImageUrl() != null) {

            fileStorageService.delete(
                    complaint.getImageUrl(),
                    FileType.COMPLAINT
            );
        }

        complaintRepository.delete(complaint);
    }

    /* =========================================
       FEEDBACK
    ========================================= */

    @Override
    public Feedback giveFeedback(
            Long complaintId,
            Feedback feedback,
            String studentEmail
    ) {

        Complaint complaint =
                getComplaintByIdForStudent(
                        complaintId,
                        studentEmail
                );

        feedback.setComplaint(complaint);

        return feedbackRepository.save(
                feedback
        );
    }

    /* =========================================
       COMPLAINT HISTORY
    ========================================= */

    @Override
    public List<ComplaintStatusHistory>
    getComplaintTracking(
            Long complaintId,
            String studentEmail
    ) {

        getComplaintByIdForStudent(
                complaintId,
                studentEmail
        );

        return complaintStatusHistoryRepository
                .findByComplaintId(complaintId);
    }

    /* =========================================
       PRIVATE HELPERS
    ========================================= */

    private Student getStudentByEmail(
            String studentEmail
    ) {

        return studentRepository
                .findByStudentEmail(studentEmail)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Student not found."
                        ));
    }

    private void validateImage(
            MultipartFile imageFile
    ) {

        if (imageFile == null ||
                imageFile.isEmpty()) {

            return;
        }

        List<String> allowedTypes = List.of(
                "image/jpeg",
                "image/png",
                "image/jpg"
        );

        if (!allowedTypes.contains(
                imageFile.getContentType()
        )) {

            throw new InvalidFileException(
                    "Only JPG and PNG images are allowed."
            );
        }

        if (imageFile.getSize() >
                MAX_IMAGE_SIZE) {

            throw new InvalidFileException(
                    "Image size must be less than 2 MB."
            );
        }



    }

    private ComplaintResponseDto mapToDto(
            Complaint complaint
    ) {

        return new ComplaintResponseDto(

                complaint.getId(),

                complaint.getTitle(),

                complaint.getDescription(),

                complaint.getImageUrl() == null
                        ? null
                        : fileStorageService.getFileUrl(
                        complaint.getImageUrl(),
                        FileType.COMPLAINT
                ),

                complaint.getAdminMessage(),

                complaint.getStatus(),

                complaint.getComplaintCategory(),

                complaint.getCreatedAt()

        );
    }
}
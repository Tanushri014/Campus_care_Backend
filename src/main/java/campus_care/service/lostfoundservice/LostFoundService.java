package campus_care.service.lostfoundservice;

import campus_care.dto.LostFoundDTO;
import campus_care.dto.LostFoundResponseDto;
import campus_care.entity.LostFound;
import campus_care.entity.Student;
import campus_care.enums.FileType;
import campus_care.exception.InvalidFileException;
import campus_care.exception.ResourceNotFoundException;
import campus_care.exception.UnauthorizedAcessException;
import campus_care.repository.LostFoundRepository;
import campus_care.repository.StudentRepository;
import campus_care.service.EmailService;
import campus_care.service.storage.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LostFoundService {

    private static final long MAX_IMAGE_SIZE =
            2 * 1024 * 1024;

    private static final List<String> ALLOWED_IMAGE_TYPES =
            List.of(
                    "image/jpeg",
                    "image/png",
                    "image/jpg"
            );

    private final LostFoundRepository lostFoundRepository;

    private final StudentRepository studentRepository;

    private final EmailService emailService;

    private final FileStorageService fileStorageService;

    /* =========================================
       CREATE ITEM
    ========================================= */

    public LostFoundResponseDto createItem(

            LostFoundDTO dto,

            String studentEmail,

            MultipartFile imageFile
    ) {

        Student student = getStudentByEmail(studentEmail);

        validateImage(imageFile);

        LostFound item = new LostFound();

        item.setTitle(dto.getTitle());

        item.setDescription(dto.getDescription());

        item.setType(dto.getType().toUpperCase());

        item.setStudent(student);

        item.setCreatedAt(LocalDateTime.now());

        if (imageFile != null && !imageFile.isEmpty()) {

            String imageName =
                    fileStorageService.store(
                            imageFile,
                            FileType.LOST_FOUND
                    );

            item.setImageUrl(imageName);
        }

        LostFound savedItem =
                lostFoundRepository.save(item);

        sendNotificationEmails(student, savedItem);

        return mapToDto(savedItem);
    }

    /* =========================================
       GET ALL ITEMS
    ========================================= */

    public Page<LostFoundResponseDto> getAllItems(

            int page,

            int size
    ) {

        Pageable pageable = PageRequest.of(

                page,

                size,

                Sort.by("createdAt").descending()
        );

        return lostFoundRepository
                .findAll(pageable)
                .map(this::mapToDto);
    }

    /* =========================================
       DELETE ITEM
    ========================================= */

    public void deleteItem(
            Long itemId,
            String studentEmail
    ) {

        Student student =
                getStudentByEmail(studentEmail);

        LostFound item =
                getItemById(itemId);

        if (!item.getStudent()
                .getId()
                .equals(student.getId())) {

            throw new UnauthorizedAcessException(
                    "You can delete only your own item."
            );
        }

        if (item.getImageUrl() != null) {

            fileStorageService.delete(
                    item.getImageUrl(),
                    FileType.LOST_FOUND
            );
        }

        lostFoundRepository.delete(item);
    }

    /* =========================================
       VALIDATE IMAGE
    ========================================= */

    private void validateImage(
            MultipartFile imageFile
    ) {

        if (imageFile == null ||
                imageFile.isEmpty()) {
            return;
        }

        if (!ALLOWED_IMAGE_TYPES.contains(
                imageFile.getContentType()
        )) {

            throw new InvalidFileException(
                    "Only JPG and PNG images are allowed."
            );
        }

        if (imageFile.getSize() >
                MAX_IMAGE_SIZE) {

            throw new InvalidFileException(
                    "Image size should be less than 2 MB."
            );
        }
    }

    /* =========================================
       GET STUDENT
    ========================================= */

    private Student getStudentByEmail(
            String studentEmail
    ) {

        return studentRepository
                .findByStudentEmail(studentEmail)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Student not found."
                        )
                );
    }

    /* =========================================
       GET ITEM
    ========================================= */

    private LostFound getItemById(
            Long itemId
    ) {

        return lostFoundRepository
                .findById(itemId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Item not found."
                        )
                );
    }

    /* =========================================
       SEND EMAIL NOTIFICATIONS
    ========================================= */

    private void sendNotificationEmails(
            Student uploader,
            LostFound item
    ) {

        List<Student> students =
                studentRepository.findAll();

        String subject =
                "New Lost/Found Item Uploaded";

        String body =
                "A new Lost/Found item has been uploaded.\n\n"
                        + "Title: "
                        + item.getTitle()
                        + "\n\nDescription: "
                        + item.getDescription()
                        + "\n\nPlease login to Campus Care.";

        for (Student student : students) {

            if (student.getStudentEmail().equals(
                    uploader.getStudentEmail()
            )) {

                continue;
            }

            emailService.sendEmail(
                    student.getStudentEmail(),
                    subject,
                    body
            );
        }
    }

    public LostFoundResponseDto getItem(Long id) {

        LostFound item = getItemById(id);

        return mapToDto(item);
    }

    private LostFoundResponseDto mapToDto(
            LostFound item
    ) {

        return new LostFoundResponseDto(

                item.getId(),

                item.getTitle(),

                item.getDescription(),

                item.getType(),

                fileStorageService.getFileUrl(
                        item.getImageUrl(),
                        FileType.LOST_FOUND
                ),

                item.getCreatedAt(),

                item.getStudent().getFirstName(),

                item.getStudent().getLastName()

        );
    }
}
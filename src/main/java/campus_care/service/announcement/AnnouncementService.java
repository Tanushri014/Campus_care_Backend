package campus_care.service.announcement;

import campus_care.dto.AnnouncementDto;
import campus_care.entity.Admin;
import campus_care.entity.Announcement;
import campus_care.enums.AdminCategory;
import campus_care.enums.FileType;
import campus_care.exception.InvalidFileException;
import campus_care.exception.ResourceNotFoundException;
import campus_care.exception.UnauthorizedAcessException;
import campus_care.repository.AdminRepository;
import campus_care.repository.AnnouncementRepository;
import campus_care.service.storage.FileStorageService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnnouncementService {

    private final AdminRepository adminRepository;

    private final AnnouncementRepository announcementRepository;

    private final AsyncEmailService asyncEmailService;

    private final FileStorageService fileStorageService;

    // =====================================================
    // CREATE ANNOUNCEMENT
    // =====================================================

    @Transactional
    public AnnouncementDto createAnnouncement(
            String adminEmail,
            String title,
            String description,
            MultipartFile file
    ) {

        Admin admin = validateMainAdmin(adminEmail);

        validatePdfFile(file);

        log.info(
                "Admin '{}' is creating an announcement.",
                adminEmail
        );

        String fileName =
                fileStorageService.store(
                        file,
                        FileType.NOTICE
                );

        Announcement savedAnnouncement =
                saveAnnouncement(
                        admin,
                        title,
                        description,
                        fileName
                );

        log.info(
                "Announcement '{}' created successfully.",
                savedAnnouncement.getId()
        );

        asyncEmailService.sendAnnouncementEmails(
                admin.getId(),
                savedAnnouncement
        );

        return mapToDto(savedAnnouncement);
    }

    // =====================================================
    // VALIDATE MAIN ADMIN
    // =====================================================

    private Admin validateMainAdmin(
            String adminEmail
    ) {

        Admin admin =
                adminRepository
                        .findByEmail(adminEmail)
                        .orElseThrow(() ->
                                new UnauthorizedAcessException(
                                        "Admin not found"
                                )
                        );

        if (admin.getCategory() != AdminCategory.MAIN) {

            throw new UnauthorizedAcessException(
                    "Only MAIN admin can create announcements"
            );
        }

        return admin;
    }

    // =====================================================
    // VALIDATE PDF FILE
    // =====================================================

    private void validatePdfFile(
            MultipartFile file
    ) {

        if (file == null || file.isEmpty()) {

            throw new InvalidFileException(
                    "File is required"
            );
        }

        if (file.getSize() > 5 * 1024 * 1024) {

            throw new InvalidFileException(
                    "File size exceeds 5 MB limit"
            );
        }

        if (!"application/pdf".equals(file.getContentType())) {

            throw new InvalidFileException(
                    "Only PDF files are allowed"
            );
        }
    }

    // =====================================================
    // SAVE ANNOUNCEMENT
    // =====================================================

    private Announcement saveAnnouncement(
            Admin admin,
            String title,
            String description,
            String fileName
    ) {

        Announcement announcement = new Announcement();

        announcement.setTitle(title);

        announcement.setDescription(description);

        announcement.setFileUrl(fileName);

        announcement.setAdmin(admin);

        return announcementRepository.save(
                announcement
        );
    }

    // =====================================================
    // GET ALL ANNOUNCEMENTS
    // =====================================================

    public Page<AnnouncementDto> getAllAnnouncements(
            int page,
            int size
    ) {

        Pageable pageable =
                PageRequest.of(
                        page,
                        size,
                        Sort.by("createdAt").descending()
                );

        Page<Announcement> announcements =
                announcementRepository.findAll(pageable);

        return announcements.map(this::mapToDto);
    }

    // =====================================================
    // MAP ENTITY TO DTO
    // =====================================================

    private AnnouncementDto mapToDto(
            Announcement announcement
    ) {

        return new AnnouncementDto(
                announcement.getId(),
                announcement.getTitle(),
                announcement.getDescription(),
                fileStorageService.getFileUrl(
                        announcement.getFileUrl(),
                        FileType.NOTICE
                ),

                announcement.getCreatedAt()
        );
    }

    // =====================================================
    // DELETE ANNOUNCEMENT
    // =====================================================

    public void deleteAnnouncement(
            String adminEmail,
            Long annId
    ) {

        validateMainAdmin(adminEmail);

        Announcement announcement =
                announcementRepository
                        .findById(annId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Announcement not found"
                                )
                        );

        // Delete PDF from storage
        fileStorageService.delete(
                announcement.getFileUrl(),
                FileType.NOTICE
        );

        announcementRepository.delete(announcement);
    }

    // =====================================================
    // UPDATE ANNOUNCEMENT
    // =====================================================

    public Announcement updateAnnouncement(
            Long annId,
            Announcement updatedAnnouncement
    ) {

        Announcement existing =
                announcementRepository
                        .findById(annId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Announcement not found"
                                )
                        );

        existing.setTitle(updatedAnnouncement.getTitle());

        existing.setDescription(updatedAnnouncement.getDescription());

        existing.setFileUrl(updatedAnnouncement.getFileUrl());

        return announcementRepository.save(existing);
    }

    public String getDownloadUrl(Long id) {

        Announcement announcement =
                announcementRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Announcement not found"
                                )
                        );

        return fileStorageService.getDownloadUrl(
                announcement.getFileUrl(),
                FileType.NOTICE
        );
    }
}
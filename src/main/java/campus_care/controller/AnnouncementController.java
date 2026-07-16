package campus_care.controller;

import campus_care.dto.AnnouncementDto;
import campus_care.service.announcement.AnnouncementService;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController

@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AnnouncementController {

    private final AnnouncementService announcementService;

    /* =========================================
       GET ALL ANNOUNCEMENTS
    ========================================= */

    @GetMapping("/announcements")
    public ResponseEntity<Page<AnnouncementDto>> getAllAnnouncements(

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "5")
            int size
    ) {

        return ResponseEntity.ok(
                announcementService.getAllAnnouncements(
                        page,
                        size
                )
        );
    }

    /* =========================================
       CREATE ANNOUNCEMENT
    ========================================= */

    @PostMapping("/admin/announcements")
    public ResponseEntity<AnnouncementDto> createAnnouncement(

            Authentication authentication,

            @RequestParam("title")
            String title,

            @RequestParam("description")
            String description,

            @RequestParam("file")
            MultipartFile file
    ) {

        return ResponseEntity.ok(
                announcementService.createAnnouncement(
                        authentication.getName(),
                        title,
                        description,
                        file
                )
        );
    }

    /* =========================================
       DELETE ANNOUNCEMENT
    ========================================= */

    @DeleteMapping("/admin/announcements/{id}")
    public ResponseEntity<String> deleteAnnouncement(

            @PathVariable Long id,

            Authentication authentication
    ) {

        announcementService.deleteAnnouncement(
                authentication.getName(),
                id
        );

        return ResponseEntity.ok(
                "Announcement deleted successfully"
        );
    }


    @GetMapping("/announcements/{id}/download")
    public ResponseEntity<String> downloadAnnouncement(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                announcementService.getDownloadUrl(id)
        );
    }
}
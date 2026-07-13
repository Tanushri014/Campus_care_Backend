package campus_care.controller;

import campus_care.dto.LostFoundDTO;
import campus_care.dto.LostFoundResponseDto;
import campus_care.service.lostfoundservice.LostFoundService;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/lostfound")
@RequiredArgsConstructor
@CrossOrigin
public class LostFoundController {

    private final LostFoundService lostFoundService;

    /* =========================================
       CREATE LOST & FOUND ITEM
    ========================================= */

    @PostMapping(
            value = "/lost-found",
            consumes = "multipart/form-data"
    )
    public ResponseEntity<LostFoundResponseDto> createLostFoundItem(

            Authentication authentication,

            @RequestParam("title")
            String title,

            @RequestParam("description")
            String description,

            @RequestParam("type")
            String type,

            @RequestParam(
                    value = "image",
                    required = false
            )
            MultipartFile image
    ) {

        LostFoundDTO dto = new LostFoundDTO();

        dto.setTitle(title);
        dto.setDescription(description);
        dto.setType(type);

        LostFoundResponseDto response =
                lostFoundService.createItem(
                        dto,
                        authentication.getName(),
                        image
                );

        return new ResponseEntity<>(
                response,
                HttpStatus.CREATED
        );
    }

    /* =========================================
       GET ALL LOST & FOUND ITEMS
    ========================================= */

    @GetMapping("/lost-found")
    public ResponseEntity<Page<LostFoundResponseDto>> getAllLostFoundItems(

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size
    ) {

        return ResponseEntity.ok(
                lostFoundService.getAllItems(
                        page,
                        size
                )
        );
    }

    /* =========================================
       GET SINGLE LOST & FOUND ITEM
    ========================================= */

    @GetMapping("/lost-found/{id}")
    public ResponseEntity<LostFoundResponseDto> getLostFoundItem(

            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                lostFoundService.getItem(id)
        );
    }

    /* =========================================
       DELETE LOST & FOUND ITEM
    ========================================= */

    @DeleteMapping("/lost-found/{id}")
    public ResponseEntity<String> deleteLostFoundItem(

            @PathVariable Long id,

            Authentication authentication
    ) {

        lostFoundService.deleteItem(
                id,
                authentication.getName()
        );

        return ResponseEntity.ok(
                "Item deleted successfully."
        );
    }
}
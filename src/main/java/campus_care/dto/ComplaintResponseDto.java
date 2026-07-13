package campus_care.dto;

import campus_care.enums.ComplaintCategory;
import campus_care.enums.ComplaintStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ComplaintResponseDto {

    private Long id;

    private String title;

    private String description;

    private String imageUrl;

    private String adminMessage;

    private ComplaintStatus status;

    private ComplaintCategory complaintCategory;

    private LocalDateTime createdAt;

}
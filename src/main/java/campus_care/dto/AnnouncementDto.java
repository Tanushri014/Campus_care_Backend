package campus_care.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnnouncementDto {

    private Long id;

    private String title;

    private String description;

    private String fileUrl;

    private LocalDateTime createdAt;
}
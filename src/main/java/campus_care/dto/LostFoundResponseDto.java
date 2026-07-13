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
public class LostFoundResponseDto {

    private Long id;

    private String title;

    private String description;

    private String type;

    private String imageUrl;

    private LocalDateTime createdAt;

    private String firstName;

    private String lastName;

}
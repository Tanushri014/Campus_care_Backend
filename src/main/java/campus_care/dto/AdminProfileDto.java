package campus_care.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AdminProfileDto {

    private Long id;

    private String email;

    private String category;
}
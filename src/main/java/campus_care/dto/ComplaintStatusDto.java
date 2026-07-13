package campus_care.dto;

import jakarta.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ComplaintStatusDto {

    @NotBlank
    private String status;

    private String message;
}
package campus_care.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifyCollegeIdRequest {


    @Email
    @NotBlank
    private String studentEmail;

    @NotBlank
    private String collegeId;
}
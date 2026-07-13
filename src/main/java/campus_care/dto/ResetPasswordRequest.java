package campus_care.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ResetPasswordRequest {

    @Email
    @NotBlank
    private String studentEmail;

    @NotBlank
    private String newPassword;

    @NotBlank
    private String confirmPassword;
}
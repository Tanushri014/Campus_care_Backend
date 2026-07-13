package campus_care.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class VerifyOtpRequest {

    @Email(message = "Enter a valid email")
    @NotBlank(message = "Email is required")
    private String studentEmail;

    @NotNull(message = "OTP is required")
    @Min(100000)
    @Max(999999)
    private Integer otp;
}
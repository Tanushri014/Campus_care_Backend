package campus_care.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResendOtpRequest {

    @Email
    @NotBlank
    private String studentEmail;

}

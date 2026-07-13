package campus_care.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StudentLoginRequest {
    @Email
    @NotBlank
    private String studentEmail;

    @NotBlank
    private String password;
}


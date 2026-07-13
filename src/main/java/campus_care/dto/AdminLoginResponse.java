package campus_care.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AdminLoginResponse {

    private Long id;

    private String email;

    private String role;

    private String category;

    private String token;
}
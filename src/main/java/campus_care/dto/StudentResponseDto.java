package campus_care.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentResponseDto {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String collegeId;

    private String token;
}
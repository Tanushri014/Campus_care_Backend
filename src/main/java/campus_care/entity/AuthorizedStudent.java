package campus_care.entity;

import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name="authorized_students")
public class AuthorizedStudent {

    @Id
    private String collegeId;

    private String firstName;

    private String lastName;

    private String department;

    private Integer year;

    private boolean claimed;

    private String claimedByEmail;
}
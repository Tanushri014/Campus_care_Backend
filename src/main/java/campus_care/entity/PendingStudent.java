package campus_care.entity;

import jakarta.persistence.*;
import lombok.*;

import campus_care.enums.AuthProvider;
import java.time.LocalDateTime;
import campus_care.enums.RegistrationStatus;
@Entity
@Table(name = "pending_students")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PendingStudent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;

    private String lastName;

    @Column(unique = true, nullable = false)
    private String studentEmail;

    private String password;

    @Column(unique = true)
    private String collegeId;

    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    @Enumerated(EnumType.STRING)
    private RegistrationStatus status;

    private LocalDateTime createdAt;
}
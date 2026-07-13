package campus_care.entity;

import campus_care.enums.AdminCategory;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ADMINS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Admin {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            unique = true,
            nullable = false,
            length = 150
    )
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AdminCategory category;
}
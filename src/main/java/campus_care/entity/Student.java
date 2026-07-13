package campus_care.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;


import campus_care.enums.AuthProvider;
import java.util.List;

@Entity
@Table(name = "students")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String studentEmail;

    @JsonIgnore
    private String password;

    @Column(unique = true)
    private String collegeId;

    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    private boolean emailVerified;

    private boolean collegeVerified;
    @JsonIgnore
    @OneToMany(mappedBy = "student")
    private List<Complaint> complaints;


    @JsonIgnore
    @OneToMany(mappedBy = "student")
    private List<LostFound> lostFoundItems;
}
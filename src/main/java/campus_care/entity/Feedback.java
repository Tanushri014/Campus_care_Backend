package campus_care.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;

import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="feedback")
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int rating;

    @Column(name = "comment")
    private String message;

    private LocalDateTime createdAt;

    @OneToOne
    @JoinColumn(
            name = "complaint_id",
            unique = true
    )
    @JsonIgnoreProperties({
            "student",
            "feedback"
    })
    private Complaint complaint;

    @PrePersist
    private void onCreate() {

        createdAt = LocalDateTime.now();
    }
}
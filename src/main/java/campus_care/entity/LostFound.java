package campus_care.entity;

import jakarta.persistence.*;

import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "lost_found")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LostFound {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(
            nullable = false,
            length = 2000
    )
    private String description;

    @Column(nullable = false)
    private String type;

    private String imageUrl;

    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id",nullable=false)
    private Student student;

    @PrePersist
    private void onCreate() {

        createdAt = LocalDateTime.now();
    }
}
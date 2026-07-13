package campus_care.entity;

import campus_care.enums.ComplaintCategory;
import campus_care.enums.ComplaintStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "complaints")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Complaint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            nullable = false,
            length = 150
    )
    private String title;

    @Column(
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String description;

    private String imageUrl;

    @Column(name = "admin_message")
    private String adminMessage;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ComplaintStatus status;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "complaint_category",
            nullable = false
    )
    private ComplaintCategory complaintCategory;

    @Column(
            nullable = false,
            updatable = false
    )
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {

        this.createdAt = LocalDateTime.now();
    }

    /* =========================================
       STUDENT RELATION
       ========================================= */

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "student_id",
            nullable = false
    )
    @JsonIgnoreProperties({
            "complaints",
            "password"
    })
    private Student student;

    /* =========================================
       STATUS HISTORY
       ========================================= */

    @OneToMany(
            mappedBy = "complaint",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JsonIgnore
    private List<ComplaintStatusHistory> statusHistory;

    /* =========================================
       FEEDBACK
       ========================================= */

    @OneToOne(
            mappedBy = "complaint",
            cascade = CascadeType.ALL
    )
    @JsonIgnore
    private Feedback feedback;

    /* =========================================
       ENUMS
       ========================================= */




}
package campus_care.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

import jakarta.persistence.Table;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="EmailOtp")
public class EmailOtp{

    @Id
    @GeneratedValue
    private Long id;

    private String studentEmail;

    private Integer otp;

    private LocalDateTime expiryTime;

    private boolean verified;
}
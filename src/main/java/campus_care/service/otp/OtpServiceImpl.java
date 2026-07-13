package campus_care.service.otp;

import campus_care.dto.VerifyOtpRequest;
import campus_care.entity.EmailOtp;
import campus_care.repository.OtpRepository;
import campus_care.service.EmailService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import campus_care.exception.ResourceNotFoundException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import campus_care.exception.InvalidRequestException;
@Service
@RequiredArgsConstructor
@Slf4j
public class OtpServiceImpl implements OtpService {

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final int OTP_VALIDITY_MINUTES = 2;

    private final OtpRepository otpRepository;
    private final EmailService emailService;

    private Integer generateOtp() {

        log.info("Generating OTP...");

        return 100000 + RANDOM.nextInt(900000);
    }

    @Override
    @Transactional
    public void sendOtp(String studentEmail) {

        Integer otp = generateOtp();

        EmailOtp emailOtp = otpRepository
                .findByStudentEmail(studentEmail)
                .orElse(new EmailOtp());

        emailOtp.setStudentEmail(studentEmail);
        emailOtp.setOtp(otp);
        emailOtp.setExpiryTime(
                LocalDateTime.now().plusMinutes(OTP_VALIDITY_MINUTES)
        );
        emailOtp.setVerified(false);

        otpRepository.save(emailOtp);

        String subject = "Campus Care - Email Verification";

        String body = """
                Hello,

                Your OTP for Campus Care registration is:

                %d

                This OTP is valid for 2 minutes.

                If you did not request this registration,
                please ignore this email.

                Regards,
                Campus Care Team
                """.formatted(otp);

        log.info("Sending OTP email to {}", studentEmail);

        emailService.sendEmail(
                studentEmail,
                subject,
                body
        );

        log.info("OTP email sent successfully.");
    }

    @Override
    @Transactional
    public void verifyOtp(VerifyOtpRequest dto) {

        log.info("Verifying OTP for {}", dto.getStudentEmail());

        EmailOtp emailOtp = otpRepository
                .findByStudentEmail(dto.getStudentEmail())
                .orElseThrow(() ->
                        new ResourceNotFoundException("OTP not found. Please register again.")
                );

        if (emailOtp.isVerified()) {
            throw new InvalidRequestException("OTP has already been verified.");
        }

        if (emailOtp.getExpiryTime().isBefore(LocalDateTime.now())) {

            otpRepository.delete(emailOtp);

            throw new InvalidRequestException(
                    "OTP has expired. Please request a new OTP."
            );
        }

        if (!emailOtp.getOtp().equals(dto.getOtp())) {

            throw new InvalidRequestException("Invalid OTP.");
        }

        emailOtp.setVerified(true);

        otpRepository.save(emailOtp);

        log.info("OTP verified successfully.");
    }

    @Override
    @Transactional
    public void deleteOtp(String studentEmail) {

        otpRepository.deleteByStudentEmail(studentEmail);

        log.info("Deleted OTP for {}", studentEmail);
    }



    //delete expired otp after evry 2 minutes
   @Transactional
    @Scheduled(fixedRate = 60000)
    public void deleteExpiredOtp() {
        otpRepository.deleteByExpiryTimeBefore(LocalDateTime.now());
    }

}
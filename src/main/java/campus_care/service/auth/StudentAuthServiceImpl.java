package campus_care.service.auth;
import campus_care.dto.*;

import campus_care.enums.RegistrationStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import campus_care.repository.StudentRepository;
import campus_care.service.otp.OtpService;
import campus_care.jwt.JwtUtil;
import campus_care.repository.AuthorizedStudentRepository;
import campus_care.enums.AuthProvider;
import campus_care.entity.AuthorizedStudent;
import campus_care.entity.Student;
import campus_care.repository.PendingStudentRepository;
import campus_care.entity.PendingStudent;
import java.time.LocalDateTime;
import campus_care.service.EmailService;
@Service
@Slf4j
@RequiredArgsConstructor
public class StudentAuthServiceImpl implements StudentAuthService {

    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;
    private final OtpService otpService;
private final AuthorizedStudentRepository authorizedStudentRepository;
    private final JwtUtil jwtUtil;
private final EmailService emailService;
    private final PendingStudentRepository pendingStudentRepository;

    @Override
    public void register(StudentRegistrationRequest dto) {

        // Registration completed
        if (studentRepository.findByStudentEmail(dto.getStudentEmail()).isPresent()) {
            throw new RuntimeException("Email is already registered.");
        }

        // Registration already started
        PendingStudent existingPending = pendingStudentRepository
                .findByStudentEmail(dto.getStudentEmail())
                .orElse(null);

        if (existingPending != null) {

            existingPending.setFirstName(dto.getFirstName());
            existingPending.setLastName(dto.getLastName());
            existingPending.setPassword(
                    passwordEncoder.encode(dto.getPassword())
            );

            existingPending.setCreatedAt(LocalDateTime.now());

            existingPending.setStatus(
                    RegistrationStatus.OTP_PENDING
            );

            pendingStudentRepository.save(existingPending);

            otpService.deleteOtp(existingPending.getStudentEmail());

            otpService.sendOtp(existingPending.getStudentEmail());

            return;
        }

        PendingStudent pendingStudent = new PendingStudent();

        pendingStudent.setFirstName(dto.getFirstName());
        pendingStudent.setLastName(dto.getLastName());
        pendingStudent.setStudentEmail(dto.getStudentEmail());
        pendingStudent.setPassword(passwordEncoder.encode(dto.getPassword()));
        pendingStudent.setProvider(AuthProvider.LOCAL);
        pendingStudent.setStatus(RegistrationStatus.OTP_PENDING);
        pendingStudent.setCreatedAt(LocalDateTime.now());

        pendingStudentRepository.save(pendingStudent);

        otpService.sendOtp(dto.getStudentEmail());
    }
    @Override
    public void verifyOtp(VerifyOtpRequest dto) {

        otpService.verifyOtp(
                dto
        );

        PendingStudent pendingStudent =
                pendingStudentRepository
                        .findByStudentEmail(dto.getStudentEmail())
                        .orElseThrow(() ->
                                new RuntimeException("Pending registration not found.")
                        );

        pendingStudent.setStatus(
                RegistrationStatus.COLLEGE_VERIFICATION_PENDING
        );

        pendingStudentRepository.save(pendingStudent);
    }
@Transactional
    @Override
    public LoginResponse verifyCollegeId(VerifyCollegeIdRequest dto) {

        PendingStudent pendingStudent =
                pendingStudentRepository
                        .findByStudentEmail(dto.getStudentEmail())
                        .orElseThrow(() ->
                                new RuntimeException("Pending registration not found.")
                        );

        if (pendingStudent.getStatus() != RegistrationStatus.COLLEGE_VERIFICATION_PENDING) {

            throw new RuntimeException("Please verify your email first.");
        }

        AuthorizedStudent authorizedStudent =
                authorizedStudentRepository
                        .findByCollegeId(dto.getCollegeId())
                        .orElseThrow(() ->
                                new RuntimeException("Invalid College ID.")
                        );

        if (authorizedStudent.isClaimed()) {

            throw new RuntimeException("This College ID has already been used.");
        }

        Student student = new Student();

        student.setFirstName(pendingStudent.getFirstName());
        student.setLastName(pendingStudent.getLastName());
        student.setStudentEmail(pendingStudent.getStudentEmail());
        student.setPassword(pendingStudent.getPassword());

        student.setCollegeId(dto.getCollegeId());

        student.setProvider(pendingStudent.getProvider());

        student.setEmailVerified(true);
        student.setCollegeVerified(true);

        studentRepository.save(student);

        authorizedStudent.setClaimed(true);
        authorizedStudent.setClaimedByEmail(
                pendingStudent.getStudentEmail()
        );

    pendingStudentRepository.delete(pendingStudent);

    otpService.deleteOtp(student.getStudentEmail());
try{
    emailService.sendEmail(
            student.getStudentEmail(),
            "Successful Registration to CampusCare",
            "Welcome to CampusCare System"
    );
}
   catch(Exception e){
    log.error("falied to send mail",e);
   }

    String token = jwtUtil.generateToken(
            student.getStudentEmail(),
            "STUDENT",
            null
    );

    return new LoginResponse(
            token,
            "Registration Successful",
            "STUDENT"
    );
    }
    @Override
    public LoginResponse login(StudentLoginRequest dto) {

        Student student = studentRepository
                .findByStudentEmail(dto.getStudentEmail())
                .orElseThrow(() ->
                        new RuntimeException("Student not found.")
                );

        if (student.getProvider() != AuthProvider.LOCAL) {

            throw new RuntimeException(
                    "Please login using " + student.getProvider()
            );
        }

        if (!passwordEncoder.matches(
                dto.getPassword(),
                student.getPassword())) {

            throw new RuntimeException(
                    "Invalid email or password."
            );
        }

        String token = jwtUtil.generateToken(
                student.getStudentEmail(),
                "STUDENT",
                null
        );

        return new LoginResponse(
                token,
                "Login Successful",
                "STUDENT"
        );
    }
    @Override
    public Student processGoogleLogin(
            String email,
            String firstName,
            String lastName
    ) {

        Student existingStudent = studentRepository
                .findByStudentEmail(email)
                .orElse(null);

        if (existingStudent != null) {

            if (existingStudent.getProvider() != AuthProvider.GOOGLE) {

                throw new RuntimeException(
                        "This email is already registered using LOCAL login."
                );
            }

            return existingStudent;
        }

        PendingStudent pendingStudent = pendingStudentRepository
                .findByStudentEmail(email)
                .orElse(null);

        if (pendingStudent == null) {

            pendingStudent = new PendingStudent();

            pendingStudent.setFirstName(firstName);
            pendingStudent.setLastName(lastName);
            pendingStudent.setStudentEmail(email);

            pendingStudent.setProvider(AuthProvider.GOOGLE);

            pendingStudent.setStatus(
                    RegistrationStatus.COLLEGE_VERIFICATION_PENDING
            );

            pendingStudent.setCreatedAt(LocalDateTime.now());

            pendingStudentRepository.save(pendingStudent);
            log.info("Saved pending user {}", pendingStudent.getStudentEmail());
        }

        // Student doesn't exist yet
        return null;
    }
    @Override
    public void resendOtp(ResendOtpRequest dto) {

        PendingStudent pendingStudent =
                pendingStudentRepository
                        .findByStudentEmail(dto.getStudentEmail())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Pending registration not found."
                                )
                        );

        otpService.deleteOtp(
                pendingStudent.getStudentEmail()
        );

        otpService.sendOtp(
                pendingStudent.getStudentEmail()
        );
    }

    @Transactional
    @Scheduled(fixedRate = 900000) // every 15 minutes
    public void deletePendingRegistrations() {
        pendingStudentRepository
                .deleteByCreatedAtBefore(LocalDateTime.now().minusMinutes(15));
    }

    @Override
    public void forgotPassword(ForgotPasswordRequest dto) {

        Student student = studentRepository
                .findByStudentEmail(dto.getStudentEmail())
                .orElseThrow(() ->
                        new RuntimeException("Student with this email does not exist.")
                );

        if (student.getProvider() == AuthProvider.GOOGLE) {

            throw new RuntimeException(
                    "This account uses Google Sign-In. Please continue with Google."
            );
        }

        otpService.deleteOtp(student.getStudentEmail());

        otpService.sendOtp(student.getStudentEmail());
    }

    @Override
    public void verifyForgotPasswordOtp(VerifyOtpRequest dto) {

        otpService.verifyOtp(dto);

    }

    @Override
    public void resetPassword(ResetPasswordRequest dto) {

        Student student = studentRepository
                .findByStudentEmail(dto.getStudentEmail())
                .orElseThrow(() ->
                        new RuntimeException("Student not found.")
                );

        if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {

            throw new RuntimeException("Passwords do not match.");
        }

        student.setPassword(
                passwordEncoder.encode(dto.getNewPassword())
        );

        studentRepository.save(student);

        otpService.deleteOtp(student.getStudentEmail());
    }

}
package campus_care.service.auth;

import campus_care.dto.LoginResponse;
import campus_care.dto.ResendOtpRequest;
import campus_care.dto.StudentLoginRequest;
import campus_care.dto.StudentRegistrationRequest;
import campus_care.dto.VerifyCollegeIdRequest;
import campus_care.dto.VerifyOtpRequest;
import campus_care.entity.Student;
import campus_care.dto.ForgotPasswordRequest;
import campus_care.dto.ResetPasswordRequest;
public interface StudentAuthService {

    void register(StudentRegistrationRequest dto);

    void verifyOtp(VerifyOtpRequest dto);

    LoginResponse verifyCollegeId(VerifyCollegeIdRequest dto);

    LoginResponse login(StudentLoginRequest dto);

    Student processGoogleLogin(
            String email,
            String firstName,
            String lastName
    );
    void forgotPassword(ForgotPasswordRequest dto);

    void verifyForgotPasswordOtp(VerifyOtpRequest dto);

    void resetPassword(ResetPasswordRequest dto);
void deletePendingRegistrations();
    void resendOtp(ResendOtpRequest dto);
}
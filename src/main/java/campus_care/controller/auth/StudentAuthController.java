package campus_care.controller.auth;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpServletResponse;


import campus_care.util.CookieUtil;
import campus_care.dto.ResetPasswordRequest;
import campus_care.dto.ForgotPasswordRequest;
import campus_care.service.auth.StudentAuthService;
import campus_care.dto.StudentRegistrationRequest;
import campus_care.dto.StudentLoginRequest;
import campus_care.dto.LoginResponse;
import campus_care.dto.ResendOtpRequest;
import campus_care.dto.VerifyOtpRequest;
import campus_care.dto.VerifyCollegeIdRequest;
import campus_care.dto.ApiResponse;


@RestController
@RequestMapping("/auth")

@RequiredArgsConstructor
public class StudentAuthController {

    private final StudentAuthService studentAuthService;
private final CookieUtil cookieUtil;
    @PostMapping("/register")
    public ResponseEntity<String> register(
            @RequestBody StudentRegistrationRequest dto) {

        studentAuthService.register(dto);

        return ResponseEntity.ok("OTP sent successfully.");
    }


    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(
            @RequestBody VerifyOtpRequest dto) {

        studentAuthService.verifyOtp(dto);

        return ResponseEntity.ok("OTP verified successfully.");
    }

    @PostMapping("/verify-college-id")
    public ResponseEntity<LoginResponse> verifyCollegeId(
            @RequestBody VerifyCollegeIdRequest dto,
            HttpServletResponse response) {

        LoginResponse loginResponse =
                studentAuthService.verifyCollegeId(dto);

        cookieUtil.addJwtCookie(
                response,
                loginResponse.getToken()
        );

        return ResponseEntity.ok(loginResponse);
    }
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody StudentLoginRequest dto,
            HttpServletResponse response) {

        LoginResponse loginResponse =
                studentAuthService.login(dto);

        cookieUtil.addJwtCookie(
                response,
                loginResponse.getToken()
        );

        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/resend-otp")
    public ResponseEntity<String> resendOtp(
            @RequestBody ResendOtpRequest dto
    ) {

        studentAuthService.resendOtp(dto);

        return ResponseEntity.ok(
                "OTP sent successfully."
        );
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest dto) {

        studentAuthService.forgotPassword(dto);

        return ResponseEntity.ok(
                new ApiResponse(
                        "OTP sent successfully to your email.",
                        true
                )
        );
    }

    @PostMapping("/verify-forgot-password-otp")
    public ResponseEntity<ApiResponse> verifyForgotPasswordOtp(
            @Valid @RequestBody VerifyOtpRequest dto) {

        studentAuthService.verifyForgotPasswordOtp(dto);

        return ResponseEntity.ok(
                new ApiResponse(
                        "OTP verified successfully.",
                        true
                )
        );
    }


    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse> resetPassword(
            @Valid @RequestBody ResetPasswordRequest dto) {

        studentAuthService.resetPassword(dto);

        return ResponseEntity.ok(
                new ApiResponse(
                        "Password updated successfully. Please login.",
                        true
                )
        );
    }


    @PostMapping("/logout")
    public ResponseEntity<ApiResponse> logout(
            HttpServletResponse response
    ) {

        cookieUtil.clearJwtCookie(response);

        return ResponseEntity.ok(
                new ApiResponse(
                        "Logged out successfully.",
                        true
                )
        );
    }


}

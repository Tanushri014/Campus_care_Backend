package campus_care.config;

import campus_care.entity.Student;
import campus_care.jwt.JwtUtil;
import campus_care.service.auth.StudentAuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import campus_care.util.CookieUtil;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuthSuccessHandler
        implements AuthenticationSuccessHandler {

    private final StudentAuthService studentAuthService;
private final CookieUtil cookieUtil;
    private final JwtUtil jwtUtil;

    @Value("${frontend.url}")
    private String frontendUrl;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {

        OAuth2User oauthUser =
                (OAuth2User) authentication.getPrincipal();

        String email =
                oauthUser.getAttribute("email");

        String name =
                oauthUser.getAttribute("name");

        String firstName = "";

        String lastName = "";

        if (name != null && !name.isBlank()) {

            String[] parts = name.split(" ");

            firstName = parts[0];

            if (parts.length > 1) {
                lastName = parts[1];
            }
        }

        Student student =
                studentAuthService.processGoogleLogin(
                        email,
                        firstName,
                        lastName
                );

        // New Google user -> PendingStudent created
        if (student == null) {

            response.sendRedirect(
                    frontendUrl +
                            "/verify-college?email=" +
                            email
            );

            return;
        }

        // Existing Google user but college not verified
        if (!student.isCollegeVerified()) {

            response.sendRedirect(
                    frontendUrl +
                            "/verify-college?email=" +
                            student.getStudentEmail()
            );

            return;
        }

        String token = jwtUtil.generateToken(
                student.getStudentEmail(),
                "STUDENT",
                null
        );

        cookieUtil.addJwtCookie(
                response,
                token
        );

        response.sendRedirect(
                frontendUrl + "/oauth-success"
        );
    }
}
package campus_care.service.auth;

import campus_care.dto.AdminLoginRequest;
import campus_care.dto.AdminLoginResponse;
import campus_care.entity.Admin;
import campus_care.jwt.JwtUtil;
import campus_care.repository.AdminRepository;
import campus_care.exception.InvalidCredentialsException;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminAuthService {

    private final AdminRepository adminRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtil jwtUtil;

    public AdminLoginResponse login(
            AdminLoginRequest request
    ) {

        Admin admin = adminRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new InvalidCredentialsException(
                                "Invalid email or password"
                        )
                );

        validatePassword(
                request.getPassword(),
                admin.getPassword()
        );

        String token = jwtUtil.generateToken(
                admin.getEmail(),
                "ADMIN",
                admin.getCategory().name()
        );

        return buildResponse(
                admin,
                token
        );
    }

    /* =========================================
       PRIVATE HELPERS
    ========================================= */

    private void validatePassword(

            String rawPassword,

            String encodedPassword
    ) {

        if (!passwordEncoder.matches(
                rawPassword,
                encodedPassword
        )) {

            throw new InvalidCredentialsException(
                    "Invalid email or password"
            );
        }
    }

    private AdminLoginResponse buildResponse(

            Admin admin,

            String token
    ) {

        return new AdminLoginResponse(

                admin.getId(),

                admin.getEmail(),

                "ADMIN",

                admin.getCategory().name(),

                token
        );
    }
}
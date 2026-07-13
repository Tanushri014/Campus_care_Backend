package campus_care.controller.auth;

import campus_care.dto.AdminLoginRequest;
import campus_care.dto.AdminLoginResponse;
import campus_care.service.auth.AdminAuthService;
import campus_care.util.CookieUtil;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletResponse;


@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminAuthController {

    private final AdminAuthService adminAuthService;
private final CookieUtil cookieUtil;

    @PostMapping("/login")
    public ResponseEntity<AdminLoginResponse> login(
            @Valid
            @RequestBody AdminLoginRequest request,
            HttpServletResponse httpResponse
    ) {

        AdminLoginResponse response =
                adminAuthService.login(request);

        cookieUtil.addJwtCookie(
                httpResponse,
                response.getToken()
        );

        return ResponseEntity.ok(response);
    }
}
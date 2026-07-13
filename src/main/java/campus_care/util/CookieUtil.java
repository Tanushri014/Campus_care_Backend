package campus_care.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CookieUtil {

    @Value("${cookie.secure}")
    private boolean secure;

    public void addJwtCookie(
            HttpServletResponse response,
            String token
    ) {

        Cookie cookie = new Cookie("jwt", token);

        cookie.setHttpOnly(true);
        cookie.setSecure(secure);
        cookie.setPath("/");
        cookie.setMaxAge(24 * 60 * 60);
        cookie.setAttribute("SameSite", "None");

        response.addCookie(cookie);
    }

    public void clearJwtCookie(
            HttpServletResponse response
    ) {

        Cookie cookie = new Cookie("jwt", "");

        cookie.setHttpOnly(true);
        cookie.setSecure(secure);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        cookie.setAttribute("SameSite", "None");

        response.addCookie(cookie);
    }
}
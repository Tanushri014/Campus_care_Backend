package campus_care.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.context.annotation.Configuration;
@Configuration
public class AppConfig {
//to keep the password encrpted
    @Bean
    //bcrypt because its slow and it protects from attaacks .
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

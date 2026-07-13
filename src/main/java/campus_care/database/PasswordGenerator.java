package campus_care.database;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordGenerator {

    public static void main(String[] args) {

        BCryptPasswordEncoder encoder =
                new BCryptPasswordEncoder();

        System.out.println(encoder.encode("main123"));
        System.out.println(encoder.encode("hostel123"));
        System.out.println(encoder.encode("mess123"));
        System.out.println(encoder.encode("acad123"));
        System.out.println(encoder.encode("campus123"));
        System.out.println(encoder.encode("bus123"));
    }
}
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordGenerator {

    public static String passGenerator() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "world";
        return encoder.encode(rawPassword);
    }
}

package t3h.manga.mangaweb.components.verification;
import java.security.SecureRandom;
import java.util.Base64;

public class VerificationCodeGenerator {

    private static final int CODE_LENGTH = 8; // Độ dài của mã xác nhận

    public static String generateRandomCode() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] randomBytes = new byte[CODE_LENGTH];
        secureRandom.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }
}


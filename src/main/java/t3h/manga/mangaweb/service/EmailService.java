package t3h.manga.mangaweb.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    public void sendVerificationCode(String email, String verificationCode) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject("Xác minh địa chỉ email");
        mailMessage.setText("Mã xác minh của bạn là: " + verificationCode);

        emailSender.send(mailMessage);
    }
}

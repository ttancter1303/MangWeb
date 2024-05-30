package t3h.manga.mangaweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import t3h.manga.mangaweb.model.Account;
import t3h.manga.mangaweb.model.Manga;
import t3h.manga.mangaweb.repository.AccountRepository;
import t3h.manga.mangaweb.service.EmailService;
import t3h.manga.mangaweb.service.MangaService;

import java.util.List;

import static t3h.manga.mangaweb.components.verification.VerificationCodeGenerator.generateRandomCode;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("/profile")
    public String getUserProfile(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails != null) {
            String username = userDetails.getUsername();
            String email = getEmailFromPrincipal(userDetails);
            model.addAttribute("username", username);
            model.addAttribute("email", email);
        }

        model.addAttribute("content", "frontend/user_profile.html");
        return "layouts/layout.html";
    }
    @GetMapping("/forgot-password")
    public String showForgotPasswordForm(Model model) {
        model.addAttribute("content", "frontend/email.html");
        return "layouts/layout.html";
    }

    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestParam("email") String email) {
        // Kiểm tra xem email có tồn tại trong hệ thống không
        Account user = accountRepository.findAccountByEmail(email);
        if (user != null) {
            // Tạo mã xác nhận ngẫu nhiên và lưu vào cơ sở dữ liệu
            String verificationCode = generateRandomCode();
            user.setVerificationCode(verificationCode);
            accountRepository.save(user);

            // Gửi email chứa mã xác nhận
            emailService.sendVerificationCode(email, verificationCode);
        }
        // Redirect hoặc hiển thị thông báo tùy thuộc vào kết quả
        return "redirect:/";
    }
    @GetMapping("/reset-password")
    public String showResetPasswordForm(Model model) {
        model.addAttribute("content", "frontend/reset_password.html");
        return "layouts/layout.html";
    }
    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam("email") String email,
                                @RequestParam("verificationCode") String verificationCode,
                                @RequestParam("newPassword") String newPassword) {
        // Kiểm tra xem mã xác nhận có đúng không
        Account user = accountRepository.findByEmailAndVerificationCode(email, verificationCode);
        if (user != null) {
            // Reset mật khẩu
            user.setPassword(passwordEncoder.encode(newPassword));
            // Xóa mã xác nhận sau khi mật khẩu đã được đổi
            user.setVerificationCode(null);
            accountRepository.save(user);
            // Redirect hoặc hiển thị thông báo mật khẩu đã được đổi thành công
            return "redirect:/login";
        } else {
            // Redirect hoặc hiển thị thông báo lỗi nếu mã xác nhận không đúng
            return "redirect:/forgot-password";
        }
    }
private String getEmailFromPrincipal(UserDetails userDetails) {
    if (userDetails instanceof OAuth2User) {
        OAuth2User oauth2User = (OAuth2User) userDetails;
        return oauth2User.getAttribute("email");
    } else {
        Account account = accountRepository.findAccountByUsername(userDetails.getUsername());
        return account.getEmail();
    }
}
}

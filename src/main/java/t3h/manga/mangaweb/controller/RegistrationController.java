package t3h.manga.mangaweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import t3h.manga.mangaweb.model.Account;
import t3h.manga.mangaweb.repository.AccountRepository;

@Controller
public class RegistrationController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("account", new Account());
        model.addAttribute("title", "register");
        model.addAttribute("content", "frontend/register.html");
        return "layouts/layout.html";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute Account account, Model model) {
        if (accountRepository.findAccountByUsername(account.getUsername()) != null) {
            model.addAttribute("error", "Username already exists!");
            return "redirect:/register";
        }
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        account.setRole("USER");
        accountRepository.save(account);
        return "redirect:/login";
    }
}

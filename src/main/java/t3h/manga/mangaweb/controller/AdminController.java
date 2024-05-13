package t3h.manga.mangaweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.GetMapping;



@Controller
@RequestMapping("/admin")
public class AdminController {
    @GetMapping("/")
    public String getDashboard(HttpSession session, Model model) {

        model.addAttribute("title", "Dashboard");

        model.addAttribute("content", "backend/index.html");

        return "layouts/adminlte3.html";
    }
    
}

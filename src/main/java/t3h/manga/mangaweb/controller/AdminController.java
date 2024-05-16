package t3h.manga.mangaweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.GetMapping;
import t3h.manga.mangaweb.model.Manga;
import t3h.manga.mangaweb.repository.MangaRepository;

import java.util.List;


@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    MangaRepository mangaRepository;
    @GetMapping("/dashboard")
    public String getDashboard(HttpSession session, Model model) {

        model.addAttribute("title", "Dashboard");

        model.addAttribute("content", "backend/index.html");

        return "layouts/adminlte3.html";
    }
    @GetMapping("/")
    public String getAdminPage(HttpSession session, Model model) {
        List<Manga> mangas = mangaRepository.findAll();
        model.addAttribute("title", "Page");
        model.addAttribute("mangas", mangas);
        model.addAttribute("content", "backend/mangas.html");
        return "layouts/adminlte3";
    }
    @GetMapping("/mangas/search")
    public String searchMangas(@RequestParam(required = false) String name,
                               @RequestParam(required = false) String tag,
                               Model model) {
        List<Manga> mangas;

        if ((name != null && !name.isEmpty()) && (tag != null && !tag.isEmpty())) {
            mangas = mangaRepository.findByNameContainingAndTagName(name, tag);
        } else if (name != null && !name.isEmpty()) {
            mangas = mangaRepository.findMangaByName(name);
        } else if (tag != null && !tag.isEmpty()) {
            mangas = mangaRepository.findByTagName(tag);
        } else {
            mangas = mangaRepository.findAll();
        }

        model.addAttribute("mangas", mangas);
        return "backend/mangas";
    }
    
}

package t3h.manga.mangaweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import t3h.manga.mangaweb.model.Manga;
import t3h.manga.mangaweb.service.MangaService;

import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private MangaService mangaService;

//    @GetMapping("/profile")
//    public String getUserProfile(Model model, @AuthenticationPrincipal UserDetails userDetails) {
//        List<Manga> savedMangas = mangaService.getSavedMangasForUser(userDetails.getUsername());
//        model.addAttribute("savedMangas", savedMangas);
//        model.addAttribute("content", "frontend/user_profile.html");
//        return "layouts/layout.html";
//    }
//
//    @PostMapping("/saveManga")
//    public String saveManga(@RequestParam("mangaId") Integer mangaId, @AuthenticationPrincipal UserDetails userDetails) {
//        mangaService.saveMangaForUser(userDetails.getUsername(), mangaId);
//        return "redirect:/manga/" + mangaId;
//    }
}

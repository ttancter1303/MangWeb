package t3h.manga.mangaweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import t3h.manga.mangaweb.service.MangaRankingService;

import java.util.List;

@Controller
public class MangaRankingController {

    @Autowired
    private MangaRankingService mangaRankingService;

    @GetMapping("/rankings")
    public String getTopRankedMangas(Model model) {
        List<MangaRankingService.MangaRankingDTO> mangaRankings = mangaRankingService.getTopRankedMangas();
        model.addAttribute("mangaRankings", mangaRankings);
        model.addAttribute("content", "frontend/rankings.html");
        return "layouts/layout.html";
    }
}

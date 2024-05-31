//package t3h.manga.mangaweb.controller;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import t3h.manga.mangaweb.model.Manga;
//import t3h.manga.mangaweb.repository.MangaRepository;
//import t3h.manga.mangaweb.service.MangaRankingService;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//@Controller
//public class MangaRankingController {
//
//    @Autowired
//    private MangaRankingService mangaRankingService;
//    @Autowired
//    private MangaRepository mangaRepository;
//
//    @GetMapping("/rankings")
//    public String getTopRankedMangas(Model model) {
//        List<MangaRankingService.MangaRankingDTO> mangaRankings = mangaRankingService.getTopRankedMangas();
//        List<Manga> mangas = new ArrayList<>();
//        for (MangaRankingService.MangaRankingDTO mangaRanking : mangaRankings) {
//            Optional<Manga> mangaOpt = mangaRepository.findById(mangaRanking.getMangaId());
//            mangaOpt.ifPresent(mangas::add);
//        }
//        model.addAttribute("mangaRankings", mangas);
//        model.addAttribute("content", "frontend/rankings.html");
//        return "layouts/layout";
//    }
//
//}

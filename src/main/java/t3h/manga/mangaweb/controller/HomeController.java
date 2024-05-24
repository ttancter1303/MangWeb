package t3h.manga.mangaweb.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import t3h.manga.mangaweb.model.Chapter;
import t3h.manga.mangaweb.model.Manga;
import t3h.manga.mangaweb.repository.ChapterRepository;
import t3h.manga.mangaweb.repository.MangaRepository;
import t3h.manga.mangaweb.repository.TagRepository;

import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("")
public class HomeController {
    @Autowired
    MangaRepository mangaRepository;
    @Autowired
    TagRepository tagRepository;
    @Autowired
    ChapterRepository chapterRepository;

    @GetMapping({ "/", "", "/index", "/home" })
    public String getHomePage(HttpSession session,
            Model model,
            @RequestParam(defaultValue = "0") int page
    // @RequestParam(defaultValue = "10") int size
    ) {
        int size = 12;
        if (page > 0) {
            page = page - 1;
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<Manga> mangaPage = mangaRepository.findAll(pageable);
        model.addAttribute("title", "NetSteal Chính Thức");
        model.addAttribute("mangaPage", mangaPage);
        long maxPage = (mangaRepository.count() % size != 0) ? (mangaRepository.count() / size + 1)
                : (mangaRepository.count() / size);
        model.addAttribute("maxPage", maxPage);
        model.addAttribute("content", "frontend/index.html");
        return "layouts/layout.html";
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("title", "Login");
        model.addAttribute("content", "frontend/login.html");
        return "layouts/layout.html";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/manga/{id}")
    public String getMangaDetail(@PathVariable("id") Integer mangaId, Model model) {
        // Lấy thông tin chi tiết của truyện từ repository (mangaRepository)
        Manga manga = mangaRepository.findById(mangaId).orElse(null);

        // Kiểm tra xem manga có tồn tại không
        if (manga != null) {
            // Lấy danh sách các chapter của manga
            List<Chapter> chapters = manga.getChapterList();

            // Chuyển thông tin truyện và danh sách chapter vào model để hiển thị trên trang
            // chi tiết
            model.addAttribute("manga", manga);
            model.addAttribute("chapters", chapters);
            model.addAttribute("title", manga.getName());
            Collections.reverse(chapters);
            // Trả về tên của trang chi tiết truyện
            model.addAttribute("content", "frontend/manga_detail.html");
            return "layouts/layout.html";
        } else {
            // Nếu không tìm thấy truyện, trả về một trang lỗi hoặc thông báo lỗi
            model.addAttribute("content", "error/error.html");
            return "layouts/layout.html";
        }
    }

}

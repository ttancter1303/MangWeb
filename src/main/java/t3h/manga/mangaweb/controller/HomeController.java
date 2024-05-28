package t3h.manga.mangaweb.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import t3h.manga.mangaweb.model.Account;
import t3h.manga.mangaweb.dto.ChapterDTO;
import t3h.manga.mangaweb.model.Chapter;
import t3h.manga.mangaweb.model.Manga;
import t3h.manga.mangaweb.repository.AccountRepository;
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
    private AccountRepository accountRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private ChapterRepository chapterRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @SuppressWarnings("null")
    @GetMapping({ "/", "", "/index", "/home" })
    public String getHomePage(HttpSession session,
            Model model,
            @RequestParam(defaultValue = "0") int page) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !(auth.getPrincipal() instanceof String)) {
            Account account;
            try {
                DefaultOAuth2User oidcUser = (DefaultOAuth2User) auth.getPrincipal();
                String email = oidcUser.getAttribute("email");
                account = accountRepository.findAccountByEmail(email);
                if (account == null) {
                    account = new Account();
                    account.setUsername(email.split("@")[0]);
                    account.setPassword(passwordEncoder.encode(auth.getName()));
                    account.setEmail(email);
                    account.setAvatar(oidcUser.getAttribute("picture"));
                    account.setRole("ROLE_USER");
                    accountRepository.save(account);
                }
            } catch (Exception e) {
                account = accountRepository.findAccountByUsername(auth.getName());
            }
            session.setAttribute("USER_LOGGED", account);
        }

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
            Chapter firstChapter = null;
            Chapter lastChapter = null;
            if (chapters != null && !chapters.isEmpty()) {
                // Lấy chapter đầu tiên và cuối cùng
                firstChapter = chapters.get(0);
                lastChapter = chapters.get(chapters.size() - 1);
            }
            // Chuyển thông tin truyện và danh sách chapter vào model để hiển thị trên trang
            // chi tiết
            model.addAttribute("manga", manga);
            model.addAttribute("chapters", chapters);
            model.addAttribute("firstChapter", firstChapter);
            model.addAttribute("lastChapter", lastChapter);
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

    @GetMapping("/manga/{mangaId}/chapter/{chapterId}")
    public String getChapter(@PathVariable("mangaId") Integer mangaId,
            @PathVariable("chapterId") Integer chapterId,
            Model model) {
        Manga manga = mangaRepository.findById(mangaId).orElse(null);

        // Kiểm tra xem manga có tồn tại không
        if (manga != null) {
            Chapter chapter = chapterRepository.findById(chapterId).orElse(null);
            if (chapter != null) {
                ChapterDTO chapterDTO = new ChapterDTO(chapter);
                List<String> listimage = chapterDTO.getPathImagesList();
                List<Chapter> chapters = manga.getChapterList();
                model.addAttribute("manga", manga);
                model.addAttribute("chapter", chapter);

                Integer chapIndex = chapters.indexOf(chapter);
                model.addAttribute("listImage", listimage);
                model.addAttribute("nextChapter",
                        (chapters.size() - 1 == chapIndex) ? null : chapters.get(chapIndex + 1));
                model.addAttribute("prevChapter",
                        (0 == chapIndex) ? null : chapters.get(chapIndex - 1));
                model.addAttribute("title", manga.getName() + " " + chapter.getName());
                // System.out.println(listimage);
                model.addAttribute("content", "frontend/chapter_detail.html");
                return "layouts/layout.html";
            }
        }
        model.addAttribute("content", "error/error.html");
        return "layouts/layout.html";

    }

    @GetMapping("/search")
    public String searchManga(@RequestParam(name = "name", required = false) String name,
            Model model) {
        List<Manga> mangas;
        if (name != null && !name.isEmpty()) {
            String searchName = "%" + name + "%";
            mangas = mangaRepository.findMangaByNameLike(searchName);
            for (Manga manga : mangas) {
                System.out.println("Manga: " + manga);
            }
        } else {
            mangas = mangaRepository.findAll();
        }
        model.addAttribute("title", "Search");
        model.addAttribute("mangas", mangas);
        model.addAttribute("content", "frontend/search_manga.html");
        return "layouts/layout.html";
    }

}

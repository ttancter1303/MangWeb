package t3h.manga.mangaweb.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import t3h.manga.mangaweb.model.Account;
import t3h.manga.mangaweb.dto.ChapterDTO;
import t3h.manga.mangaweb.model.Chapter;
import t3h.manga.mangaweb.model.Manga;
import t3h.manga.mangaweb.model.Tag;
import t3h.manga.mangaweb.model.History;
import t3h.manga.mangaweb.repository.AccountRepository;
import t3h.manga.mangaweb.repository.ChapterRepository;
import t3h.manga.mangaweb.repository.MangaRepository;
import t3h.manga.mangaweb.repository.TagRepository;
import t3h.manga.mangaweb.repository.HistoryRepository;
import t3h.manga.mangaweb.service.MangaRankingService;
import t3h.manga.mangaweb.service.SavedMangaService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import t3h.manga.mangaweb.components.helper.Pagination;

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
    SavedMangaService savedMangaService;

    @Autowired
    private HistoryRepository historyRepository;

    @Autowired
    private ChapterRepository chapterRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private MangaRankingService mangaRankingService;

    private Integer sizePerPage = 12;

    @SuppressWarnings("null")
    @GetMapping({ "/", "", "/index", "/home" })

    public String getHomePage(HttpSession session,
            Model model,
            @RequestParam(defaultValue = "1") Integer page) {
        if (page <= 0) {
            model.addAttribute("content", "error/error.html");
            return "layouts/layout.html";
        }

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
        model.addAttribute("title", "NetSteal Chính Thức");

        Pagination pagination = new Pagination(mangaRepository.count(), this.sizePerPage, page);
        model.addAttribute("pagination", pagination);

        Pageable pageable = PageRequest.of((page - 1), this.sizePerPage, Sort.by("updatedAt").descending());
        Page<Manga> mangaPage = mangaRepository.findAll(pageable);
        model.addAttribute("mangaPage", mangaPage);

        Account account = (Account) session.getAttribute("USER_LOGGED");
        model.addAttribute("history", historyRepository.findByUser(account));

        List<MangaRankingService.MangaRankingDTO> mangaRankings = mangaRankingService.getTopRankedMangas();
        List<Manga> mangas = new ArrayList<>();
        for (MangaRankingService.MangaRankingDTO mangaRanking : mangaRankings) {
            Optional<Manga> mangaOpt = mangaRepository.findById(mangaRanking.getMangaId());
            mangaOpt.ifPresent(mangas::add);
        }
        model.addAttribute("mangaRankings", mangas);
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
    public String getMangaDetail(@PathVariable("id") Integer mangaId, Model model, HttpSession session) {
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
            Account account = (Account) session.getAttribute("USER_LOGGED");
            model.addAttribute("history", historyRepository.findByUser(account));
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

    @GetMapping("/history")
    public String getHistory(Model model, HttpSession session) {
        Account account = (Account) session.getAttribute("USER_LOGGED");

        if (account != null) {
            model.addAttribute("history", historyRepository.findByUser(account));
            model.addAttribute("content", "frontend/history.html");
            return "layouts/layout.html";
        } else {
            return "redirect:/";
        }
    }

    public Resource getImage(String imageUrl) throws Exception {
        return new UrlResource(imageUrl);
    }

    @GetMapping("/manga/{mangaId}/chapter/{chapterId}")
    public String getChapter(@PathVariable("mangaId") Integer mangaId,
            @PathVariable("chapterId") Integer chapterId,
            HttpSession session,
            Model model) {
        Manga manga = mangaRepository.findById(mangaId).orElse(null);

        // Kiểm tra xem manga có tồn tại không
        if (manga != null) {
            Chapter chapter = chapterRepository.findById(chapterId).orElse(null);
            if (chapter != null) {

                Account user = (Account) session.getAttribute("USER_LOGGED");

                if (user != null) {
                    History history = historyRepository.findByUserAndManga(user, manga);
                    if (history != null) {
                        history.setUpdatedAt(LocalDateTime.now());
                    } else {
                        history = new History();
                        history.setUser(user);
                        history.setManga(manga);
                    }
                    history.setChapter(chapter);
                    historyRepository.save(history);
                }

                ChapterDTO chapterDTO = new ChapterDTO(chapter);
                List<String> listimage = chapterDTO.getPathImagesList();
                List<Resource> listimage2 = new ArrayList<>();
                List<Chapter> chapters = manga.getChapterList();
                for (String src : listimage) {
                    Resource resource = null;
                    try {
                        resource = getImage(src);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (resource != null && resource.exists()) {
                        listimage2.add(resource);
                    }
                }
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

    @PostMapping("manga/save-manga")
    public String saveManga(@RequestParam("username") String username,
            @RequestParam("mangaId") Integer mangaId,
            @RequestHeader(value = "Referer", required = false) String referer,
            Model model) {
        Account account = accountRepository.findAccountByUsername(username);
        Optional<Manga> mangaOpt = mangaRepository.findById(mangaId);
        if (mangaOpt.isPresent()) {
            Manga manga = mangaOpt.get();
            String message = savedMangaService.saveManga(account, manga);
            model.addAttribute("message", message);
        }

        if (referer != null) {
            return "redirect:" + referer;
        }

        return "redirect:/";
    }


    // TODO: Pagination for search page
    @GetMapping("/search")
    public String searchManga(@RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "tag", required = false) String tag,
            // @RequestParam(defaultValue = "1") Integer page,
            Model model) {
        List<Manga> mangas;
        if (tag != null && !tag.isEmpty()) {
            String tagSlug = tag;
            Tag getTag = tagRepository.findBySlug(tagSlug);
            if (getTag != null) {
                mangas = mangaRepository.findByTagName(getTag.getName());
            } else {
                mangas = mangaRepository.findAll();
            }
        } else if (name != null && !name.isEmpty()) {
            mangas = mangaRepository.findMangaByNameLike(name);
        } else {
            mangas = mangaRepository.findAll();
        }

        // Pagination pagination = new Pagination(mangaRepository.count(), this.sizePerPage, page);
        // model.addAttribute("pagination", pagination);

        model.addAttribute("title", "Search");
        model.addAttribute("mangas", mangas);
        model.addAttribute("content", "frontend/search_manga.html");
        return "layouts/layout.html";
    }

}

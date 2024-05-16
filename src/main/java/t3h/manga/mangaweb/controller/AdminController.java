package t3h.manga.mangaweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import t3h.manga.mangaweb.model.Chapter;
import t3h.manga.mangaweb.model.Manga;
import t3h.manga.mangaweb.model.Tag;
import t3h.manga.mangaweb.repository.ChapterRepository;
import t3h.manga.mangaweb.repository.MangaRepository;
import t3h.manga.mangaweb.repository.TagRepository;

import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    MangaRepository mangaRepository;
    @Autowired
    TagRepository tagRepository;
    @Autowired
    ChapterRepository chapterRepository;
    @GetMapping("/dashboard")
    public String getDashboard(HttpSession session, Model model) {

        model.addAttribute("title", "Dashboard");

        model.addAttribute("content", "backend/index.html");

        return "layouts/adminlte3.html";
    }
    @GetMapping("")
    public String getAdminPage(HttpSession session,
                               Model model,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Manga> mangaPage = mangaRepository.findAll(pageable);
        System.out.println(mangaPage.getTotalPages());
        model.addAttribute("title", "Page");
        model.addAttribute("mangaPage", mangaPage); // Sửa từ "mangas" thành "mangaPage"
        model.addAttribute("content", "backend/mangas.html");
        return "layouts/adminlte3";
    }
    @GetMapping("/mangas/search")
    public String searchManga(@RequestParam(name = "name", required = false) String name,
                              @RequestParam(name = "tag", required = false) String tag,
                              Model model) {
        List<Manga> mangas;
        if (name != null && !name.isEmpty()) {
            mangas = mangaRepository.findMangaByName(name);
        } else if (tag != null && !tag.isEmpty()) {
            mangas = mangaRepository.findByTagName(tag);
        } else {
            mangas = mangaRepository.findAll();
        }
        model.addAttribute("title", "Page");
        model.addAttribute("mangas", mangas);
        model.addAttribute("content", "backend/mangas.html");
        return "layouts/adminlte3";
    }
    @GetMapping("/mangas/delete/{id}")
    public String deleteManga(@PathVariable("id") Integer id) {
        mangaRepository.deleteById(id);
        return "redirect:/admin"; // Redirect to manga list page after deletion
    }
    @GetMapping("/mangas/edit/{id}")
    public String showEditForm(@PathVariable("id") Integer id, Model model) {
        Manga manga = mangaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid manga Id:" + id));
        List<Tag> mangaTags = manga.getListTag();
        List<Tag> allTags = tagRepository.findAll();
        List<Chapter> chapters = chapterRepository.findByMangaId(id);

        model.addAttribute("title", "Edit Manga");
        model.addAttribute("manga", manga);
        model.addAttribute("mangaTags", mangaTags);
        model.addAttribute("allTags", allTags);
        model.addAttribute("chapters", chapters);
        model.addAttribute("content", "backend/edit-manga.html");
        return "layouts/adminlte3";
    }

    @PostMapping("/mangas/edit/{id}")
    public String editManga(@PathVariable("id") Integer id,
                            @ModelAttribute("manga") Manga updatedManga,
                            @RequestParam(value = "selectedTags", required = false) List<Integer> selectedTags) {
        Manga manga = mangaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid manga Id:" + id));

        manga.setName(updatedManga.getName());
        manga.setAuthor(updatedManga.getAuthor());

        if (selectedTags != null && !selectedTags.isEmpty()) {
            List<Tag> tags = tagRepository.findAllById(selectedTags);
            manga.setListTag(tags);
        } else {
            manga.setListTag(new ArrayList<>());
        }
        mangaRepository.save(manga);

        return "redirect:/admin";
    }
    @GetMapping("/mangas/{mangaId}/add-chapter")
    public String showAddChapterForm(@PathVariable("mangaId") Long mangaId, Model model) {
        model.addAttribute("mangaId", mangaId);
        return "backend/add-chapter";
    }
    @PostMapping("/mangas/{mangaId}/add-chapter")
    public String addChapter(@PathVariable("mangaId") Integer mangaId,
                             @RequestParam("name") String name,
                             @RequestParam("images") MultipartFile[] images) {
        Manga manga = mangaRepository.findById(mangaId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid manga Id:" + mangaId));

        // Tạo một đối tượng Chapter mới
        Chapter chapter = new Chapter();
        chapter.setName(name);
        // Xử lý và lưu hình ảnh vào đối tượng chapter ở đây

        // Lưu chapter vào danh sách chapter của manga
        manga.getChapterList().add(chapter);
        mangaRepository.save(manga);

        return "redirect:/admin/mangas/edit/" + mangaId; // Redirect về trang chỉnh sửa manga sau khi thêm chapter
    }
    // Phương thức để lưu hình ảnh vào thư mục lưu trữ
//    private String saveImage(MultipartFile file) {
        // Code để lưu hình ảnh vào thư mục lưu trữ và trả về đường dẫn của hình ảnh
        // Ví dụ:
        // String imagePath = "uploads/" + file.getOriginalFilename();
        // File newFile = new File(imagePath);
        // file.transferTo(newFile);
        // return imagePath;
//    }
    
}

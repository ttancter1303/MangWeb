package t3h.manga.mangaweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

import t3h.manga.mangaweb.model.Manga;
import t3h.manga.mangaweb.model.Tag;
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
    @GetMapping("/dashboard")
    public String getDashboard(HttpSession session, Model model) {

        model.addAttribute("title", "Dashboard");

        model.addAttribute("content", "backend/index.html");

        return "layouts/adminlte3.html";
    }
    @GetMapping("")
    public String getAdminPage(HttpSession session, Model model) {
        List<Manga> mangas = mangaRepository.findAll();
        model.addAttribute("title", "Page");
        model.addAttribute("mangas", mangas);
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

        // Truy vấn danh sách các tag của bộ truyện
        List<Tag> mangaTags = manga.getListTag();

        // Truy vấn tất cả các tag từ cơ sở dữ liệu
        List<Tag> allTags = tagRepository.findAll();

        // Truyền manga, danh sách tag của bộ truyện và tất cả các tag sang giao diện người dùng
        model.addAttribute("title", "Edit Manga");
        model.addAttribute("manga", manga);
        model.addAttribute("mangaTags", mangaTags); // Danh sách tag của bộ truyện
        model.addAttribute("allTags", allTags);

        // Chỉ định template và content cho giao diện người dùng
        model.addAttribute("content", "backend/edit-manga.html");
        return "layouts/adminlte3";
    }

    @PostMapping("/mangas/edit/{id}")
    public String editManga(@PathVariable("id") Integer id, @ModelAttribute("manga") Manga updatedManga, @RequestParam(value = "selectedTags", required = false) List<Integer> selectedTags) {
        // Lấy manga từ cơ sở dữ liệu
        Manga manga = mangaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid manga Id:" + id));

        // Cập nhật thông tin của manga
        manga.setName(updatedManga.getName());
        manga.setAuthor(updatedManga.getAuthor());

        // Nếu danh sách các tag đã được chọn không rỗng, cập nhật danh sách tag của manga
        if (selectedTags != null && !selectedTags.isEmpty()) {
            List<Tag> tags = tagRepository.findAllById(selectedTags);
            manga.setListTag(tags);
        } else {
            manga.setListTag(new ArrayList<>()); // Nếu không có tag nào được chọn, xóa hết tag của manga
        }

        // Lưu manga đã chỉnh sửa vào cơ sở dữ liệu
        mangaRepository.save(manga);

        // Chuyển hướng người dùng về trang chủ sau khi chỉnh sửa thành công
        return "redirect:/admin";
    }

    
}

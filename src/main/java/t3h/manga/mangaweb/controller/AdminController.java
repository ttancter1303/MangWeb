package t3h.manga.mangaweb.controller;

import com.github.junrar.Archive;
import com.github.junrar.exception.RarException;
import com.github.junrar.rarfile.FileHeader;
import com.github.junrar.volume.FileVolumeManager;
import org.hibernate.boot.archive.spi.ArchiveException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import t3h.manga.mangaweb.components.file.FileUtils;
import t3h.manga.mangaweb.model.Chapter;
import t3h.manga.mangaweb.model.Manga;
import t3h.manga.mangaweb.model.Tag;
import t3h.manga.mangaweb.repository.ChapterRepository;
import t3h.manga.mangaweb.repository.MangaRepository;
import t3h.manga.mangaweb.repository.TagRepository;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    MangaRepository mangaRepository;
    @Autowired
    TagRepository tagRepository;
    @Autowired
    ChapterRepository chapterRepository;
    @GetMapping("")
    public String admin(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        model.addAttribute("username", userDetails.getUsername());
        return "layouts/adminlte3.html";
    }
    @GetMapping("/dashboard")
    public String getDashboard(HttpSession session, Model model) {

        model.addAttribute("title", "Dashboard");

        model.addAttribute("content", "backend/index.html");

        return "layouts/adminlte3.html";
    }
    @GetMapping("/")
    public String getAdminPage(HttpSession session,
                               Model model,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Manga> mangaPage = mangaRepository.findAll(pageable);
        System.out.println(mangaPage.getTotalPages());
        model.addAttribute("title", "Admin Page");
        model.addAttribute("mangaPage", mangaPage);
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
                            @RequestParam(value = "selectedTags", required = false) List<Integer> selectedTags,
                            RedirectAttributes redirectAttributes) {
        // Tìm manga theo id
        Manga manga = mangaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid manga Id:" + id));

        // Cập nhật các thuộc tính của manga
        manga.setName(updatedManga.getName());
        manga.setAuthor(updatedManga.getAuthor());

        // Cập nhật danh sách tag nếu có
        if (selectedTags != null && !selectedTags.isEmpty()) {
            List<Tag> tags = tagRepository.findAllById(selectedTags);
            manga.setListTag(tags);
        } else {
            manga.setListTag(new ArrayList<>());
        }

        // Lưu manga đã cập nhật
        mangaRepository.save(manga);

        // Thêm thông báo thành công và chuyển hướng
        redirectAttributes.addFlashAttribute("message", "Manga updated successfully!");
        return "redirect:/admin/";
    }

    @GetMapping("/mangas/{mangaId}/add-chapter")
    public String showAddChapterForm(@PathVariable("mangaId") Integer mangaId, Model model) {
        model.addAttribute("mangaId", mangaId);
        return "backend/add-chapter";
    }
    @PostMapping("/mangas/{mangaId}/add-chapter")
    public String handleFileUpload(@RequestParam("files") MultipartFile[] files,
                                   @RequestParam("name") String name,
                                   @PathVariable("mangaId") Integer mangaId,
                                   RedirectAttributes redirectAttributes) {
        if (files.length == 0 || files[0].isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Vui lòng chọn thư mục chứa các tệp ảnh để tải lên.");
            return "redirect:/admin/mangas/edit/" + mangaId;
        }

        List<String> imagePaths = new ArrayList<>();
        try {
            // Tạo thư mục tạm để lưu các tệp ảnh tải lên
            Path tempDir = Files.createTempDirectory("");

            for (MultipartFile file : files) {
                if (file.isEmpty()) continue;

                String fileName = file.getOriginalFilename();
                if (fileName != null && isImageFile(fileName)) {
                    File tempFile = tempDir.resolve(fileName).toFile();
                    file.transferTo(tempFile);
                    imagePaths.add(tempFile.getAbsolutePath());
                }
            }

            if (imagePaths.isEmpty()) {
                redirectAttributes.addFlashAttribute("message", "Không tìm thấy tệp ảnh trong thư mục.");
                return "redirect:/admin/mangas/edit/" + mangaId;
            }

            // Tạo đối tượng chương mới và lưu các tệp đã tải lên vào chương
            Chapter chapter = new Chapter();
            chapter.setName(name);
            chapter.setPathImagesList(String.join(",", imagePaths));

            // Lưu chương vào danh sách chương của manga
            Manga manga = mangaRepository.findById(mangaId)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy manga"));
            chapter.setManga(manga);
            manga.getChapterList().add(chapter);
            mangaRepository.save(manga);

            redirectAttributes.addFlashAttribute("message", "Bạn đã tải lên và xử lý thư mục thành công!");
        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("message", "Tải lên và xử lý thư mục thất bại do lỗi IO.");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("message", "Đã xảy ra lỗi không mong muốn.");
        }

        return "redirect:/admin/mangas/edit/" + mangaId;
    }

    // Phương thức phụ để kiểm tra tệp có phải là tệp ảnh hay không
    private boolean isImageFile(String fileName) {
        String[] imageExtensions = new String[]{"png", "jpg", "jpeg", "gif", "bmp"};
        fileName = fileName.toLowerCase();
        for (String ext : imageExtensions) {
            if (fileName.endsWith("." + ext)) {
                return true;
            }
        }
        return false;
    }



}

package t3h.manga.mangaweb.controller;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import t3h.manga.mangaweb.dto.MangaDTO;
import t3h.manga.mangaweb.model.Chapter;
import t3h.manga.mangaweb.model.Manga;
import t3h.manga.mangaweb.model.Tag;
// import t3h.manga.mangaweb.repository.AuthorRepository;
import t3h.manga.mangaweb.repository.ChapterRepository;
import t3h.manga.mangaweb.repository.MangaRepository;
import t3h.manga.mangaweb.repository.TagRepository;
import t3h.manga.mangaweb.service.CrawlerService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@RestController
@RequestMapping("/api/crawler")
public class CrawlerController {
    // @Autowired
    // AuthorRepository authorRepository;
    @Autowired
    MangaRepository mangaRepository;
    @Autowired
    TagRepository tagRepository;
    @Autowired
    CrawlerService crawlerService;
    @Autowired
    ChapterRepository chapterRepository;

    public CrawlerController(MangaRepository mangaRepository,
            TagRepository tagRepository) {
        // this.authorRepository = authorRepository;
        this.mangaRepository = mangaRepository;
        this.tagRepository = tagRepository;
    }

    @SuppressWarnings("rawtypes")
    @PostMapping("/tag")
    public ResponseEntity createTags() {
        ArrayList<Tag> tags = crawlerService.getAllTagCrawler();
        tagRepository.saveAll(tags);
        return new ResponseEntity<>("Create successfully", HttpStatus.OK);
    }

    @PostMapping("/manga")
    public ResponseEntity<?> crawManga(@RequestParam("url") String url) {
        
        if (tagRepository.count() == 0) {
            createTags();
        }

        url = url.strip();
        if (url.length() > 0) {
            HashMap<String, Object> result = crawlerService.crawlManga(url);
            if (result == null) {
                result = new HashMap<>();
                result.put("status", "Có điều gì đó không ổn đã xảy ra, hãy chắc chắn rằng bạn đang lấy đường dẫn truyện được cập nhật tại: https://nettruyenfull.com");
                return ResponseEntity.status(HttpStatus.OK).body(result);
            }
            return ResponseEntity.status(HttpStatus.OK).body(result);

        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong!");
        }
    }
}

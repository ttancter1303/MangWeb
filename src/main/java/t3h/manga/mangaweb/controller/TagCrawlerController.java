package t3h.manga.mangaweb.controller;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import t3h.manga.mangaweb.entity.Tag;
import t3h.manga.mangaweb.repository.TagRepository;

import java.io.IOException;
import java.util.ArrayList;

@RestController
@RequestMapping("/crawler/tag")
public class TagCrawlerController {
    @Autowired
    TagRepository tagRepository;
    @PostMapping("create")
    public ResponseEntity create() {
        ArrayList<String> strings = tagAfterCrawler();
        for (String string : strings) {
            Tag tag = new Tag();
            System.out.println(" tag: " + string);
            tag.setName(string);
            tagRepository.save(tag);
        }
        return new ResponseEntity<>("Create successfully", HttpStatus.OK);
    }
    public static ArrayList<String> tagAfterCrawler() {
        ArrayList<String> listTag = new ArrayList<>();
        String url = "https://blogtruyen.vn/";
        try {
            Document document = Jsoup.connect(url).get();
            Elements tags = document.select("ul.submenu.category.list-unstyled a");
            for (Element element : tags) {
                listTag.add(element.text());
                System.out.println("tag: " + element.text());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return listTag;
    }
}

package t3h.manga.mangaweb.components;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import t3h.manga.mangaweb.model.Account;
import t3h.manga.mangaweb.model.Tag;
import t3h.manga.mangaweb.repository.AccountRepository;
import t3h.manga.mangaweb.repository.TagRepository;
import t3h.manga.mangaweb.service.SlugService;

@Component
public class InsertSampleData {
    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private AccountRepository accRepository;

    @Autowired
    private SlugService slugService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void insertSampleAdmin() {
        if (accRepository.count() == 0) {
            Account acc = new Account();
            acc.setEmail("admin@email.com");
            acc.setUsername("admin");
            acc.setPassword(passwordEncoder.encode("000000"));
            acc.setRole("ROLE_ADMIN");
            accRepository.save(acc);
        }
    }

    @PostConstruct
    public void insertSampleTags() {
        if (tagRepository.count() == 0) {
            ArrayList<Tag> listTag = new ArrayList<>();
            String url = "https://nettruyenfull.com/tim-truyen";
            try {
                Document document = Jsoup.connect(url).get();
                Elements links = document.select(".genres .nav li a");
                HashSet<String> uniqueTexts = new HashSet<>();
                for (Element liElement : links) {
                    String nameTag = liElement.text();
                    if (!uniqueTexts.contains(nameTag) && !nameTag.contains("Tất cả thể loại")) {
                        uniqueTexts.add(nameTag);
                    }
                }
                Integer i = 1;
                for (String text : uniqueTexts) {
                    Tag tag = new Tag();
                    tag.setName(text);
                    String slug = slugService.convertToSlug(text);
                    tag.setSlug(slug + "-" + i);
                    listTag.add(tag);
                    i+= 1;
                }
                tagRepository.saveAll(listTag);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}

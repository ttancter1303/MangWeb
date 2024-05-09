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
import org.springframework.web.bind.annotation.RestController;
import t3h.manga.mangaweb.entity.Author;
import t3h.manga.mangaweb.entity.Manga;
import t3h.manga.mangaweb.entity.Tag;
import t3h.manga.mangaweb.repository.AuthorRepository;
import t3h.manga.mangaweb.repository.MangaRepository;
import t3h.manga.mangaweb.repository.TagRepository;
import t3h.manga.mangaweb.service.MangaService;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

@RestController
@RequestMapping("/crawler")
public class CrawlerController {
    @Autowired
    AuthorRepository authorRepository;
    @Autowired
    MangaRepository mangaRepository;
    @Autowired
    TagRepository tagRepository;
    @Autowired
    MangaService mangaService;

    public CrawlerController(AuthorRepository authorRepository, MangaRepository mangaRepository, TagRepository tagRepository) {
        this.authorRepository = authorRepository;
        this.mangaRepository = mangaRepository;
        this.tagRepository = tagRepository;
    }

    @PostMapping("/tag")
    public ResponseEntity create() {
        ArrayList<Tag> strings = getAllTagCrawler();
        tagRepository.saveAll(strings);
        return new ResponseEntity<>("Create successfully", HttpStatus.OK);
    }
    @PostMapping("/manga")
    public ResponseEntity crawManga(){
        ArrayList<String> urlList = new ArrayList<>();
        urlList.add("https://blogtruyen.vn/33314/zankokuna-kami-ga-shihaisuru");
        urlList.add("https://blogtruyen.vn/33474/gannibal-lang-an-thit-nguoi");

        for (String url : urlList) {
            try {
                Document document = Jsoup.connect(url).get();
                Elements entryTitle = document.select("h1.entry-title");
                Elements content = document.select("div.content");
                Element titleElement = document.selectFirst("span.title");
                Element listChapterElement = document.selectFirst("div.list-chapters");
                Element authorElement = document.selectFirst("a.color-green.label.label-info");
                Elements tags = document.select("ul.submenu.category.list-unstyled a");
                Element thumbnailElement = document.selectFirst("div.thumbnail");
                Element status = document.selectFirst("span.color-red");
                String imageUrl = thumbnailElement.selectFirst("img").attr("src");

                String statusTxt = status.text();


                String mangaTitle = entryTitle.text();

                Manga manga = new Manga();

                String authorName = authorElement.text();
                System.out.println("Author: "+ authorName);
                Author author = new Author();
                author.setName(authorName);
                authorRepository.save(author);

                manga = new Manga();
                manga.setName(mangaTitle);
                manga.setDescription(content.text());
                if (statusTxt.equals("Đang tiến hành")){
                    manga.setStatus(true);
                }else{
                    manga.setStatus(false);
                }
                manga.setThumbnailImg(imageUrl);
                for (String tag : getTagCrawler(url)) {
                    Tag localTag = tagRepository.findByName(tag);
                    manga.setListTag(new ArrayList<>());
                    manga.getListTag().add(localTag);

                }
                System.out.println(manga);
                mangaRepository.save(manga);

//                mangaService.saveMangaWithTags(manga,getTagCrawler(url));

                String titleText;
                String chapterTitleText;
//                if (entryTitle != null || titleElement != null) {
//                    titleText = entryTitle.text();
//                    File mangaEntryFolder = new File(mangaFolder, titleText);
//                    mangaEntryFolder.mkdir();
//
//                    if (listWrapElement != null) {
//                        Elements linkElements = listWrapElement.select("a");
//                        for (Element linkElement : linkElements) {
//                            chapterTitleText = linkElement.text();
//                            String chapterUrl = linkElement.absUrl("href");
//
//                            File chapterEntryFolder = new File(mangaEntryFolder, chapterTitleText);
//
//                            Chapter chapter = new Chapter();
//                            chapter.setName(chapterTitleText);
//                            chapter.setManga(manga);
//                            chapterRepository.save(chapter);
//
//                            chapterEntryFolder.mkdirs();
//                            System.out.println("Đã tạo thư mục: " + chapterEntryFolder.getAbsolutePath());
//                            System.out.println("Đường dẫn chap: " + chapterUrl);
//                            getImageChapter(chapterUrl);
//                        }
//                    } else {
//                        System.out.println("Không tìm thấy thẻ div có class 'list-wrap'");
//                    }
//                    System.out.println("Đã tạo thư mục: " + mangaEntryFolder.getAbsolutePath());
//                } else {
//                    System.out.println("Không tìm thấy thẻ h1 có class 'entry-title'");
//                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return new ResponseEntity("Crawler done", HttpStatus.OK);
    }
    public ArrayList<Tag> getAllTagCrawler() {
        ArrayList<Tag> listTag = new ArrayList<>();
        String url = "https://blogtruyen.vn/";
        try {
            Document document = Jsoup.connect(url).get();
            Elements tags = document.select("ul.submenu.category.list-unstyled a");
            for (Element element : tags) {
                Tag tag = new Tag();
                tag.setName(element.text());
                listTag.add(tag);
                System.out.println("tag: " + element.text());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return listTag;
    }
    public ArrayList<String> getTagCrawler(String url) {
        ArrayList<String> listTag = new ArrayList<>();
        try {
            Document document = Jsoup.connect(url).get();
            Elements tags = document.select("span.category");
            for (Element element : tags) {
                listTag.add(element.text());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return listTag;
    }
}

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

    public CrawlerController(AuthorRepository authorRepository, MangaRepository mangaRepository, TagRepository tagRepository) {
        this.authorRepository = authorRepository;
        this.mangaRepository = mangaRepository;
        this.tagRepository = tagRepository;
    }

    @PostMapping("/tag")
    public ResponseEntity create() {
        ArrayList<Tag> strings = tagAfterCrawler();
        tagRepository.saveAll(strings);
        return new ResponseEntity<>("Create successfully", HttpStatus.OK);
    }
    @PostMapping("/manga")
    public ResponseEntity crawManga(){
        ArrayList<String> urlList = new ArrayList<>();
        urlList.add("https://blogtruyen.vn/33314/zankokuna-kami-ga-shihaisuru");
        urlList.add("https://blogtruyen.vn/33396/o-long-vien-linh-vat-song");
        urlList.add("https://blogtruyen.vn/33474/gannibal-lang-an-thit-nguoi");

        for (String url : urlList) {
            try {
                Document document = Jsoup.connect(url).get();
                Elements entryTitle = document.select("h1.entry-title");
                Elements content = document.select("div.content");
                Element titleElement = document.selectFirst("span.title");
                Element listChapterElement = document.selectFirst("div.list-chapters");
                Elements authorElements = document.select("p:contains(Tác giả)").select("a");
                Elements tags = document.select("ul.submenu.category.list-unstyled a");
                Element thumbnailElement = document.selectFirst("div.thumbnail");


                String imageUrl = thumbnailElement.selectFirst("img").attr("src");

                String mangaTitle = entryTitle.text();
                Author author = null;
                Manga manga = new Manga();

                Element authorElement = authorElements.first();
                System.out.println("Author: "+authorElement.text());
//                if (!authorElements.isEmpty()) {
//                    Element authorElement = authorElements.first();
//                    author.setName(authorElement.text());
//                    System.out.println("Tác giả: " + author);
//                } else {
//                    System.out.println("Không tìm thấy thông tin về tác giả.");
//                }
//                authorRepository.save(author);


//                author = authorRepository.findAuthorByName(authorName);
//                System.out.println("author");
//                System.out.println(author);
//                if (author == null) {
//                    author = new Author(authorName);
//                            authorRepository.save(author);
//                    System.out.println(author);
//                }
//                manga = mangaRepository.findMangaByName(mangaTitle);
//                manga.setDescription(content.text());

                manga = new Manga();
                manga.setName(mangaTitle);
                manga.setDescription(content.text());
                manga.setThumbnailImg(imageUrl);
                System.out.println(manga);
                mangaRepository.save(manga);

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
    public ArrayList<Tag> tagAfterCrawler() {
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
}

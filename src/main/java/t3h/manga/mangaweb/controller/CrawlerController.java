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
import t3h.manga.mangaweb.entity.Chapter;
import t3h.manga.mangaweb.entity.Manga;
import t3h.manga.mangaweb.entity.Tag;
import t3h.manga.mangaweb.repository.AuthorRepository;
import t3h.manga.mangaweb.repository.ChapterRepository;
import t3h.manga.mangaweb.repository.MangaRepository;
import t3h.manga.mangaweb.repository.TagRepository;
import t3h.manga.mangaweb.service.MangaService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    @Autowired
    ChapterRepository chapterRepository;

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
        urlList.add("https://m.blogtruyenmoi.com/c865784/zankokuna-kami-ga-shihaisuru-25");

        for (String url : urlList) {
            try {
                Document document = Jsoup.connect(url).get();
                Elements entryTitle = document.select("h1.title");
                Elements content = document.select("article.introduce");
                Element titleChapter = document.selectFirst("span.title");
                Element listChapterElement = document.selectFirst("div.list-chapters");
//                Element authorElement = document.selectFirst("a.color-green.label.label-info");
                Elements tags = document.select("ul.submenu.category.list-unstyled a");
                Element imgElement = document.select("article.content img").first();


                String imageUrl = "";
                if (imgElement != null) {
                    imageUrl = imgElement.attr("src");
                } else {
                    imageUrl = "Image not found";
                }

                String titleText;
                String chapterTitleText;



                String mangaTitle = entryTitle.text();
//                String authorName = authorElement.text();
//                System.out.println("Author: "+ authorName);
//                Author author = new Author();
//                author.setName(authorName);

                //set thuộc tính manga
                Manga manga = new Manga();
                manga.setName(mangaTitle);
                manga.setDescription(content.text());
//                manga.setAuthor(author);
                manga.setThumbnailImg(imageUrl);
                manga.setListTag(new ArrayList<>());

                for (String tag : getTagCrawler(url)) {
                    Tag localTag = tagRepository.findByName(tag);
                    manga.getListTag().add(localTag);
                }
                manga.toString();


                System.out.println("Manga: "+manga);
//                author.getMangaList().add(manga);//add id manga vào author



                if (entryTitle != null || titleChapter != null) {
                    if (listChapterElement != null) {
                        Elements linkElements = listChapterElement.select("a");
                        for (Element linkElement : linkElements) {
                            chapterTitleText = linkElement.text();
                            String chapterUrl = linkElement.absUrl("href");
                            System.out.println("Chapter title: "+ chapterTitleText);
                            System.out.println("Chapter url: "+ chapterUrl);

                            Chapter chapter = new Chapter();
                            chapter.setName(chapterTitleText);
                            chapter.setManga(manga);

                            chapter.setImagePathList(new ArrayList<>());
                            List<String> imageUrls = getImageChapter(chapterUrl);

                            if (imageUrls == null){
                                System.out.println("Image urls rỗng: ");
                            }else {
                                for (String s : imageUrls) {
                                    System.out.println(s);
                                }
                            }
//
//
//                            for (String s : getImageChapter(chapterUrl)) {
//                                System.out.println("image url: "+s);
//                                chapter.getImagePathList().add(s);
//                            }
                            manga.getChapterList().add(chapter);
                            chapterRepository.save(chapter);
                        }
                    } else {
                        System.out.println("Không tìm thấy thẻ div có class 'list-wrap'");
                    }
                } else {
                    System.out.println("Không tìm thấy thẻ h1 có class 'entry-title'");
                }

//                authorRepository.save(author);
//                mangaRepository.save(manga);




            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return new ResponseEntity("Crawler done", HttpStatus.OK);
    }

    public ArrayList<String> getImageChapter(String chapterUrl) {
        ArrayList<String> imageChapter = new ArrayList<>();
        try {
            // Kiểm tra nếu url là "javascript:void(0)", thì bỏ qua
            if ("javascript:void(0)".equals(chapterUrl)) {
                System.out.println("Skipping invalid URL: " + chapterUrl);
                return null;
            }
            Document document = Jsoup.connect(chapterUrl).get();
            Elements imgElements = document.select("div.content");
            for (Element imgElement : imgElements) {
                String src = imgElement.attr("src");
                System.out.println("Image source: " + src);
                imageChapter.add(src);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageChapter;
    }

    public ArrayList<Tag> getAllTagCrawler() {
        ArrayList<Tag> listTag = new ArrayList<>();
        String url = "https://m.blogtruyenmoi.com/theloai/truyen-full";
        try {
            Document document = Jsoup.connect(url).get();
            Elements ulElements = document.select("div.ul-cate.ajax-content ul.list-unstyled");

            if (!ulElements.isEmpty()) {
                for (Element ulElement : ulElements) {
                    Elements liElements = ulElement.select("li.item");

                    for (Element liElement : liElements) {
                        String nameTag = liElement.text();
                        System.out.println("Tag: "+nameTag);
                        Tag tag = new Tag();
                        tag.setName(nameTag);
                        listTag.add(tag);
                    }
                }
            } else {
                System.out.println("UL elements not found");
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
            Elements tags = document.select("ul.list-unstyled li.item");
            for (Element element : tags) {
                listTag.add(element.text());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return listTag;
    }
}

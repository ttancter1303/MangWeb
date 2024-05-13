package t3h.manga.mangaweb.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import t3h.manga.mangaweb.dto.MangaDTO;
// import t3h.manga.mangaweb.model.Author;
import t3h.manga.mangaweb.model.Chapter;
import t3h.manga.mangaweb.model.Manga;
import t3h.manga.mangaweb.model.Tag;
// import t3h.manga.mangaweb.repository.AuthorRepository;
import t3h.manga.mangaweb.repository.ChapterRepository;
import t3h.manga.mangaweb.repository.MangaRepository;
import t3h.manga.mangaweb.repository.TagRepository;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CrawlerService {
    @Autowired
    TagRepository tagRepository;

    @Autowired
    MangaRepository mangaRepository;

    @Autowired
    ChapterRepository chapterRepository;

    @Autowired
    private MangaService mangaService;

    public ArrayList<Tag> getAllTagCrawler() {
        ArrayList<Tag> listTag = new ArrayList<>();
        String url = "https://nettruyenfull.com/tim-truyen";
        try {
            Document document = Jsoup.connect(url).get();
            Elements links = document.select(".genres .nav li a");
            HashSet<String> uniqueTexts = new HashSet<>();
            for (Element liElement : links) {
                String nameTag = liElement.text();
                if (!uniqueTexts.contains(nameTag)) {
                    uniqueTexts.add(nameTag);
                }
            }
            for (String text : uniqueTexts) {
                Tag tag = new Tag();
                tag.setName(text);
                listTag.add(tag);
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
            Element liTag = document.selectFirst("li.kind.row");
            Elements aTags = liTag.select("p.col-xs-8 a");
            for (Element element : aTags) {
                if (element.text().strip() != "") {
                    listTag.add(element.text());
                    // System.out.println("Tag: " + element.text());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return listTag;
    }

    public List<String> getImageChapter(String chapterUrl) {
        List<String> imageChapter = new ArrayList<>();
        try {
            // Kiểm tra nếu url là "javascript:void(0)", thì bỏ qua
            if ("javascript:void(0)".equals(chapterUrl)) {
                System.out.println("Skipping invalid URL: " + chapterUrl);
                return null;
            }
            Document document = Jsoup.connect(chapterUrl).get();
            Elements elements = document.select(".page-chapter img.lazy");
            // int i = 0;
            for (Element imgElement : elements) {
                // String src = imgElement.attr("src");
                String src = imgElement.attr("data-original");
                imageChapter.add(src);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageChapter;
    }

    public HashMap<String,Object> crawlManga(String mangaUrl) {
        HashMap<String,Object> result = new HashMap<>();
        try {
            int totalChap = 0;
            Document document = Jsoup.connect(mangaUrl).get();
            Manga manga = mangaService.getMangaBySrc(mangaUrl);
            if (manga == null) {
                manga = new Manga();
                Elements entryTitle = document.select("h1.title-detail");
                Element content = document.selectFirst(".detail-content p");
                Element authorElement = document.selectFirst(".author .col-xs-8");
                Element imgElement = document.select("div.col-image img").first();

                try {
                    String imageUrl = "";
                    if (imgElement != null) {
                        imageUrl = imgElement.attr("src");
                    } else {
                        imageUrl = "Image not found";
                    }
                    String mangaTitle = entryTitle.text();
                    if (authorElement != null) {
                        manga.setAuthor(authorElement.text());
                    } else {
                        manga.setAuthor("Đang cập nhật");
                    }
                    manga.setName(mangaTitle);
                    manga.setDescription(content.text());
                    manga.setThumbnailImg(imageUrl);
                    manga.setSource(mangaUrl);
                    manga.setListTag(new ArrayList<>());
                    for (String tag : getTagCrawler(mangaUrl)) {
                        Tag localTag = tagRepository.findByName(tag);
                        if (localTag != null)
                            manga.getListTag().add(localTag);
                    }
                    manga.toString();
                    mangaRepository.save(manga);
                } catch(Exception e) {
                    return null;
                }
            }

            List<String> getListUrlChapter = manga.getChapterList().stream().map(chap -> chap.getSource())
                    .collect(Collectors.toList());

            Element listChapterElement = document.selectFirst("div.list-chapter");
            String chapterTitleText;
            if (listChapterElement != null) {
                Elements linkElements = listChapterElement.select(".row a");
                Collections.reverse(linkElements);
                totalChap = linkElements.size();
                for (Element linkElement : linkElements) {
                    chapterTitleText = linkElement.text();
                    String chapterUrl = linkElement.absUrl("href");

                    if(!getListUrlChapter.contains(chapterUrl)) {
                        Chapter chapter = new Chapter();
                        chapter.setName(chapterTitleText);
                        chapter.setSource(chapterUrl);
                        chapter.setManga(manga);
                        String pathPagesChapter = "";
                        try {
                            for (String s : getImageChapter(chapterUrl)) {
                                pathPagesChapter += s + ",";
                            }
                            chapter.setPathImagesList(pathPagesChapter.substring(0, pathPagesChapter.length() - 1));
    
                            try {
                                chapterRepository.save(chapter);
                                manga.getChapterList().add(chapter);
                            } catch (Exception e) {
                                System.out.println("========> Loi: " + e.getMessage());
                            }
                        } catch (Exception e) {
                            System.out.println("========> Loi: " + e.getMessage());
                            break;
                        }
                    }
                }
            }
            result.put("status", "Đã cào " + manga.getChapterList().size() + "/" + totalChap + " chap.");
            result.put("manga", new MangaDTO(manga));
            return result;
        } catch (IOException e) {
            return null;
        }
    }
}

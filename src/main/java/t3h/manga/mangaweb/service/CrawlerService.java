package t3h.manga.mangaweb.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import t3h.manga.mangaweb.entity.Author;
import t3h.manga.mangaweb.entity.Chapter;
import t3h.manga.mangaweb.entity.Manga;
import t3h.manga.mangaweb.entity.Tag;
import t3h.manga.mangaweb.repository.AuthorRepository;
import t3h.manga.mangaweb.repository.ChapterRepository;
import t3h.manga.mangaweb.repository.MangaRepository;
import t3h.manga.mangaweb.repository.TagRepository;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;

@Service
public class CrawlerService {
    @Autowired
    TagRepository tagRepository;
    @Autowired
    MangaRepository mangaRepository;
    @Autowired
    AuthorRepository authorRepository;
    @Autowired
    ChapterRepository chapterRepository;


    public ArrayList<Tag> getAllTagCrawler() {
        ArrayList<Tag> listTag = new ArrayList<>();
        String url = "https://nettruyenfull.com/tim-truyen";
        try {
            Document document = Jsoup.connect(url).get();
            Elements links = document.select("div.ModuleContent .nav li a");
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
                listTag.add(element.text());
                System.out.println("Tag: "+ element.text());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return listTag;
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
            Elements elements = document.select("img.lazy");
            int i=0;
            for (Element imgElement : elements) {
                String src = imgElement.attr("src");
//                ảnh này được nhúng vào file html nên không có link
                System.out.println("Image source: " + src);
                i++;

                String[] parts = src.split(",");
                String base64Data = parts[1];
                byte[] decodedBytes = Base64.getDecoder().decode(base64Data);
                String directory = "/manga";
                String fileName = i+".jpg";
                try (FileOutputStream fos = new FileOutputStream(directory + fileName)) {
                    fos.write(decodedBytes);
                    System.out.println("Image saved successfully as: " + fileName);
                } catch (IOException e) {
                    System.err.println("Error saving image: " + e.getMessage());
                }
                imageChapter.add(src);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageChapter;
    }
    public ArrayList<Manga> crawManga(String mangaUrl){
        ArrayList<Manga> mangas = new ArrayList<>();
            try {
                Document document = Jsoup.connect(mangaUrl).get();
                Elements entryTitle = document.select("h1.title-detail");
                Element content = document.selectFirst(".detail-content");
                Element titleChapter = document.selectFirst("span.title");
                Element listChapterElement = document.selectFirst("div.list-chapter");
                Element authorElement = document.selectFirst(".author .col-xs-8");
                Element imgElement = document.select("div.col-image img").first();


                String imageUrl = "";
                if (imgElement != null) {
                    imageUrl = imgElement.attr("src");
                } else {
                    imageUrl = "Image not found";
                }

                String titleText;
                String chapterTitleText;



                String mangaTitle = entryTitle.text();
                String authorName = authorElement.text();
                System.out.println("Author: "+ authorName);
                System.out.println("Title: "+ mangaTitle);
                System.out.println("Image: "+ imageUrl);
                System.out.println("Contetn: "+ content.text());
                getTagCrawler(mangaUrl);

                Author author = new Author();
                author.setName(authorName);

                //set thuộc tính manga
                Manga manga = new Manga();
                manga.setName(mangaTitle);
                manga.setDescription(content.text());
                manga.setAuthor(author);
                manga.setThumbnailImg(imageUrl);
                manga.setListTag(new ArrayList<>());

                for (String tag : getTagCrawler(mangaUrl)) {
                    Tag localTag = tagRepository.findByName(tag);
                    manga.getListTag().add(localTag);
                }
                manga.toString();


                System.out.println("Manga: "+manga);
                author.getMangaList().add(manga);//add id manga vào author
                if (listChapterElement != null) {
                    Elements linkElements = listChapterElement.select("a");

                    for (Element linkElement : linkElements) {
                        chapterTitleText = linkElement.text();
                        String chapterUrl = linkElement.absUrl("href");
                        System.out.println("Chapter title: " + chapterTitleText);
                        System.out.println("Chapter url: " + chapterUrl);



                        Chapter chapter = new Chapter();
                        chapter.setName(chapterTitleText);
                        chapter.setManga(manga);
//                      Save chapter vào manga tương ứng và lưu đường dẫn ảnh vào các chapter
                        chapter.setImagePathList(new ArrayList<>());
//                      Lấy url của các chapter trả về arraylist
                        for (String s : getImageChapter(chapterUrl)) {
                            chapter.getImagePathList().add(s);
                        }





//                        if (manga.getChapterList() == null) {
//                            manga.setChapterList(new ArrayList<>());
//                        }
//                        manga.getChapterList().add(chapter);
//                        chapterRepository.save(chapter);
                    }
                    authorRepository.save(author);
                    mangaRepository.save(manga);


//                    for (Element linkElement : linkElements) {
//                        chapterTitleText = linkElement.text();
//                        String chapterUrl = linkElement.absUrl("href");
//                        System.out.println("Chapter title: "+ chapterTitleText);
//                        System.out.println("Chapter url: "+ chapterUrl);
//
//                        Chapter chapter = new Chapter();
//                        chapter.setName(chapterTitleText);
//                        chapter.setManga(manga);
//
//
//                        chapter.getManga().getChapterList().add(chapter);
//
//
//
//                        chapter.setImagePathList(new ArrayList<>());
//                        manga.setChapterList(new ArrayList<>());
//                        List<String> imageUrls = getImageChapter(chapterUrl);

//                        if (imageUrls == null){
//                            System.out.println("Image urls rỗng: ");
//                        }else {
//                            for (String s : imageUrls) {
//                                System.out.println(s);
//                            }
//                        }
//                        for (String s : getImageChapter(chapterUrl)) {
//                            System.out.println("image url: "+s);
//                            chapter.getImagePathList().add(s);
//                        }
//                        manga.getChapterList().add(chapter);
//                        chapterRepository.save(chapter);
//                    }
                }


            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        return mangas;
    }
}

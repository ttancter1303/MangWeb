package t3h.manga.mangaweb.crawler;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import t3h.manga.mangaweb.crawler.action.FolderProvider;
import t3h.manga.mangaweb.entity.Author;
import t3h.manga.mangaweb.entity.Chapter;
import t3h.manga.mangaweb.entity.Manga;
import t3h.manga.mangaweb.entity.Tag;
import t3h.manga.mangaweb.repository.AuthorRepository;
import t3h.manga.mangaweb.repository.ChapterRepository;
import t3h.manga.mangaweb.repository.MangaRepository;
import t3h.manga.mangaweb.repository.TagRepository;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MangaDetailCrawler implements FolderProvider {

    String titleText;
    String chapterTitleText;
    Author author;
    Tag tag;
    Manga manga;
    @Autowired
    private MangaRepository mangaRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private ChapterRepository chapterRepository;

    @Autowired
    private TagRepository tagRepository;


    public ArrayList<String> getImageChapter(String url) {
        ArrayList<String> imageChapter = new ArrayList<>();
        try {
            // Kiểm tra nếu url là "javascript:void(0)", thì bỏ qua
            if ("javascript:void(0)".equals(url)) {
                System.out.println("Skipping invalid URL: " + url);
                return null;
            }

            Document document = Jsoup.connect(url).get();
            Elements articleElements = document.select("article#content"); // Select the article with id 'content'

            for (Element articleElement : articleElements) {
                Elements imgElements = articleElement.select("img");

                for (Element imgElement : imgElements) {
                    String imageUrl = imgElement.absUrl("src");
                    System.out.println("Image URL: " + imageUrl);
                    imageChapter.add(imageUrl);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageChapter;
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
    public void mangaCrawler() {
        File mangaFolder = new File("manga");
        if (!mangaFolder.exists()) {
            mangaFolder.mkdir();
        }
        ArrayList<String> urlList = new ArrayList<>();
        urlList.add("https://blogtruyen.vn/33314/zankokuna-kami-ga-shihaisuru");
        urlList.add("https://blogtruyen.vn/33396/o-long-vien-linh-vat-song");

        for (String url : urlList) {
            try {
                Document document = Jsoup.connect(url).get();
                Elements entryTitle = document.select("h1.entry-title");
                Elements content = document.select("div.content");
                Element titleElement = document.selectFirst("span.title");
                Element listWrapElement = document.selectFirst("div.list-chapters");
                Element authorElement = document.selectFirst("a.color-green label label-info");

                String mangaTitle = entryTitle.text();
                String authorName = authorElement.text();
                if (authorElement != null) {

                    author = authorRepository.findAuthorByName(authorName);
                    System.out.println("author");
                    System.out.println(author);
                    if (author == null) {
                        author = new Author(authorName);
                        authorRepository.save(author);
                    }

                    manga = mangaRepository.findMangaByName(mangaTitle);
                    manga.setDescription(content.text());
                    if (manga == null) {
                        manga = new Manga();
                        manga.setName(mangaTitle);
                        manga.setDescription(content.text());
                        manga.setAuthor(author);
                        manga.setListTag(tagAfterCrawler());
                        mangaRepository.save(manga);
                    }
                }

                if (entryTitle != null || titleElement != null) {
                    titleText = entryTitle.text();
                    File mangaEntryFolder = new File(mangaFolder, titleText);
                    mangaEntryFolder.mkdir();

                    if (listWrapElement != null) {
                        Elements linkElements = listWrapElement.select("a");
                        for (Element linkElement : linkElements) {
                            chapterTitleText = linkElement.text();
                            String chapterUrl = linkElement.absUrl("href");

                            File chapterEntryFolder = new File(mangaEntryFolder, chapterTitleText);

                            Chapter chapter = new Chapter();
                            chapter.setName(chapterTitleText);
                            chapter.setManga(manga);
                            chapterRepository.save(chapter);

                            chapterEntryFolder.mkdirs();
                            System.out.println("Đã tạo thư mục: " + chapterEntryFolder.getAbsolutePath());
                            System.out.println("Đường dẫn chap: " + chapterUrl);
                            getImageChapter(chapterUrl);
                        }
                    } else {
                        System.out.println("Không tìm thấy thẻ div có class 'list-wrap'");
                    }
                    System.out.println("Đã tạo thư mục: " + mangaEntryFolder.getAbsolutePath());
                } else {
                    System.out.println("Không tìm thấy thẻ h1 có class 'entry-title'");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public String getFolderPath(String mangaTitle, String chapterNumber) {
        String folderPath = "manga/" + mangaTitle + "/" + chapterNumber + "/";
        return folderPath;
    }

    @Override
    public void getDataManga(String content, ArrayList<Tag> listTag, ArrayList<Chapter> listChapter, Author author) {

    }
}

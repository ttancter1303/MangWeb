package org.example;

import org.example.action.FolderProvider;
import org.example.model.Author;
import org.example.model.Chapter;
import org.example.model.Manga;
import org.example.model.Tag;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MangaDetailCrawler implements FolderProvider {

    String titleText;
    String chapterTitleText;
    Author author;
    Tag tag;
    Manga manga;



    public void getUrlChapterV2(String url) {
        try {
            // Kiểm tra nếu url là "javascript:void(0)", thì bỏ qua
            if ("javascript:void(0)".equals(url)) {
                System.out.println("Skipping invalid URL: " + url);
                return;
            }

            Document document = Jsoup.connect(url).get();
            Elements articleElements = document.select("article#content"); // Select the article with id 'content'

            for (Element articleElement : articleElements) {
                Elements imgElements = articleElement.select("img");

                for (Element imgElement : imgElements) {
                    String imageUrl = imgElement.absUrl("src");
                    System.out.println("Image URL: " + imageUrl);

                    // Sử dụng đường dẫn folder từ FolderProvider
//                    String folderPath = getFolderPath(titleText, chapterTitleText);
                    ImageCrawler imageCrawler = new ImageCrawler(this, titleText,chapterTitleText);
                    imageCrawler.imageDownloader2(imageUrl);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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


                if (authorElement != null) {
                    String authorName = authorElement.text();
                    author = new Author(authorName);
                    manga = new Manga();
                    manga.setAuthor(author);
                    manga.setDescription(content.text());
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

                            chapterEntryFolder.mkdirs();
                            System.out.println("Đã tạo thư mục: " + chapterEntryFolder.getAbsolutePath());
                            System.out.println("Đường dẫn chap: " + chapterUrl);
                            getUrlChapterV2(chapterUrl);
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

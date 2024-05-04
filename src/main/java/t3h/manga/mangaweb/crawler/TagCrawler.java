package t3h.manga.mangaweb.crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class TagCrawler {
    public ArrayList<String> tagAfterCrawler() {
        ArrayList<String> listTag = new ArrayList<>();
        String url = "https://blogtruyen.vn/";
        try {
            Document document = Jsoup.connect(url).get();
            Elements tags = document.select("ul.submenu category list-unstyled");
            Elements tag = tags.select("a");
            for (Element element : tag) {
                listTag.add(element.text());
                System.out.println("tag: " + element.text());
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}

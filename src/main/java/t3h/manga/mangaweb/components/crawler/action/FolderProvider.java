package t3h.manga.mangaweb.components.crawler.action;

// import t3h.manga.mangaweb.model.Author;
import t3h.manga.mangaweb.model.Chapter;
import t3h.manga.mangaweb.model.Tag;

import java.util.ArrayList;

public interface FolderProvider {

    String getFolderPath(String mangaTitle, String chapterNumber);
    // void getDataManga(String content, ArrayList<Tag> listTag, ArrayList<Chapter> listChapter, Author author);

}

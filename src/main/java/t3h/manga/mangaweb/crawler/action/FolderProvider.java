package t3h.manga.mangaweb.crawler.action;

import t3h.manga.mangaweb.entity.Author;
import t3h.manga.mangaweb.entity.Chapter;
import t3h.manga.mangaweb.entity.Tag;

import java.util.ArrayList;

public interface FolderProvider {

    String getFolderPath(String mangaTitle, String chapterNumber);
    void getDataManga(String content, ArrayList<Tag> listTag, ArrayList<Chapter> listChapter, Author author);

}

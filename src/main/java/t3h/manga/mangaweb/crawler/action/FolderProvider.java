package org.example.action;

import org.example.model.Author;
import org.example.model.Chapter;
import org.example.model.Tag;

import java.util.ArrayList;

public interface FolderProvider {

    String getFolderPath(String mangaTitle, String chapterNumber);
    void getDataManga(String content, ArrayList<Tag> listTag, ArrayList<Chapter> listChapter, Author author);
}

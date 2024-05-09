package t3h.manga.mangaweb.entity;

import lombok.Data;

import java.util.ArrayList;
@Data
public class MangaTagDTO {
    private String mangaTitle;
    private ArrayList<String> tagNames;
}

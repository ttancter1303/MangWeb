package t3h.manga.mangaweb.dto;

import lombok.Data;
import t3h.manga.mangaweb.model.Manga;
import t3h.manga.mangaweb.model.Tag;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class MangaTagDTO {
    public MangaTagDTO(Manga manga) {
        this.mangaTitle = manga.getName();
        this.tagNames = manga.getListTag().stream().map(tag -> tag.getName()).collect(Collectors.toList());
    }

    private String mangaTitle;
    private List<String> tagNames;
}

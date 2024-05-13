package t3h.manga.mangaweb.dto;

import t3h.manga.mangaweb.components.encrypt.AESEncryption;
import t3h.manga.mangaweb.model.Chapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChapterDTO {
    public ChapterDTO(Chapter chapter) {
        this.id = AESEncryption.encrypt(chapter.getId().toString());
        this.name = chapter.getName();
        this.views = chapter.getViews();
        this.source = chapter.getSource();
        this.createdAt = chapter.getCreatedTime();
        this.pathImagesList = new ArrayList<>(Arrays.asList(chapter.getPathImagesList().split(",")));
    }

    private String id;
    private String name;
    private Long views;
    private String source;
    private String createdAt;
    private List<String> pathImagesList;
    
}

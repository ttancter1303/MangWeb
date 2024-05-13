package t3h.manga.mangaweb.dto;

import java.util.List;
import java.util.stream.Collectors;

import t3h.manga.mangaweb.components.encrypt.AESEncryption;
import t3h.manga.mangaweb.model.Chapter;
import t3h.manga.mangaweb.model.Manga;
import t3h.manga.mangaweb.model.Tag;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class MangaDTO {
    public MangaDTO(Manga manga) {
        this.id = AESEncryption.encrypt(manga.getId().toString());
        this.name = manga.getName();
        this.description = manga.getDescription();
        this.views = manga.getChapterList().stream().mapToLong(chap -> chap.getViews()).sum();
        this.author = manga.getAuthor();
        this.thumbnailImg = manga.getThumbnailImg();
        this.source = manga.getSource();
        this.listTag = new MangaTagDTO(manga);
        this.chapterList = manga.getChapterList().stream().map(chap -> new ChapterDTO(chap))
                .collect(Collectors.toList());
    }

    private String id;
    private String name;
    private String description;
    private Long views;
    private String thumbnailImg;
    private String author;
    private String source;
    private MangaTagDTO listTag;
    private List<ChapterDTO> chapterList;
    private String createdAt;
    private String updatedAt;

}

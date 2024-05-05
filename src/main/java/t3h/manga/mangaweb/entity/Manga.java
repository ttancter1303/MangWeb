package t3h.manga.mangaweb.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
@Entity
@Data
@Table(name = "manga")
public class Manga {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer MangaID;
    private String name;
    private String description;
    private String thumbnailImg;
    private boolean status;
    @ManyToOne // Một tác giả có thể viết nhiều manga
    @JoinColumn(name = "author_id",nullable = true) // Khóa ngoại trong bảng manga
    private Author author;

    @ManyToMany // Một manga có thể có nhiều tag và một tag có thể được gắn cho nhiều manga
    @JoinTable(
            name = "manga_tag",
            joinColumns = @JoinColumn(name = "manga_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> listTag;

    @OneToMany(mappedBy = "manga", cascade = CascadeType.ALL) // Một manga có thể có nhiều chapter
    private List<Chapter> chapterList;
}

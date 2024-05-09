package t3h.manga.mangaweb.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "tag")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer tagID;
    private String name;
    @ManyToMany
    @JoinTable(
            name = "manga_tag",
            joinColumns = @JoinColumn(name = "tag_id"),
            inverseJoinColumns = @JoinColumn(name = "manga_id")
    )
    private List<Manga> mangas = new ArrayList<>();

}

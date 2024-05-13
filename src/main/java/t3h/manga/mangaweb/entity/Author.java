package t3h.manga.mangaweb.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Author")
@Data
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer authorID;
    @Column(name = "AuthorName", nullable = false)
    private String name;
    @OneToMany(mappedBy = "author", cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
    private List<Manga> mangaList = new ArrayList<>();
    public Author(String authorName) {
    }

    public Author() {

    }


}

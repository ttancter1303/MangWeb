package t3h.manga.mangaweb.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Author")
@Data
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer authorID;
    @Column(name = "AuthorName", nullable = false)
    private String name;

    public Author(String authorName) {
    }

    public Author() {

    }


}

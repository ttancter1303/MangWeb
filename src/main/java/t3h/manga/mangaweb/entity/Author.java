package t3h.manga.mangaweb.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Author")
@Data
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long authorID;
    private String name;

    public Author() {

    }

    public long getAuthorID() {
        return authorID;
    }

    public Author(String name) {
        this.name = name;
    }

    public void setAuthorID(long authorID) {
        this.authorID = authorID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Author(long authorID, String name) {
        this.authorID = authorID;
        this.name = name;
    }
}

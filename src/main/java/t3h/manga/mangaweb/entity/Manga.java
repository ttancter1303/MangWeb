package t3h.manga.mangaweb.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
@Entity
@Data
@Table(name = "manga")
public class Manga {

    public Manga() {
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String MangaID;
    private String name;
    private String description;
    private Author author;
    private String thumbnailImg;
    private List<Tag> listTag;
    private List<Chapter> listChapter;
    private boolean status;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public List<Tag> getListTag() {
        return listTag;
    }

    public void setListTag(List<Tag> listTag) {
        this.listTag = listTag;
    }

    public List<Chapter> getListChapter() {
        return listChapter;
    }

    public void setListChapter(List<Chapter> listChapter) {
        this.listChapter = listChapter;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Manga(String name, String description, Author author, List<Tag> listTag, List<Chapter> listChapter, boolean status) {
        this.name = name;
        this.description = description;
        this.author = author;
        this.listTag = listTag;
        this.listChapter = listChapter;
        this.status = status;
    }
}

package t3h.manga.mangaweb.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
@Entity
@Data
@Table(name = "chapter")
public class Chapter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer chapterID;
    private String name;
    @ManyToOne
    @JoinColumn(name = "manga_id")
    private Manga manga;
    private String date;
    @ElementCollection
    @CollectionTable(name = "image_path", joinColumns = @JoinColumn(name = "chapter_id"))
    @Column(name = "path")
    private ArrayList<String> imagePathList;

}

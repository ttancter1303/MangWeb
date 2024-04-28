package t3h.manga.mangaweb.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.awt.*;
import java.util.LinkedList;
@Entity
@Data
@Table(name = "chapter")
public class Chapter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String chapterID;
    private String name;
    private String date;
    @ElementCollection
    @CollectionTable(name = "chapter_images", joinColumns = @JoinColumn(name = "chapter_id"))
    // Thiết lập tên bảng và tên cột liên kết với khóa ngoại
    @Column(name = "image_path")
    private LinkedList<String> imagePathLinkedList;

}

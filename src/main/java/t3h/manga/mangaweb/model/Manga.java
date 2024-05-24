package t3h.manga.mangaweb.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "manga")
public class Manga {
    public Manga() {
        this.chapterList = new ArrayList<>();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String thumbnailImg;

    // @ManyToOne
    // @JoinColumn(name = "author_id", nullable = true)
    // private Author author;

    private String author;

    private String status;

    @Column(length = 1000)
    private String source;

    @ManyToMany // Một manga có thể có nhiều tag và một tag có thể được gắn cho nhiều manga
    @JoinTable(name = "manga_tag", joinColumns = @JoinColumn(name = "manga_id"), inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private List<Tag> listTag;

    @OneToMany(mappedBy = "manga", cascade = CascadeType.ALL) // Một manga có thể có nhiều chapter
    private List<Chapter> chapterList;

    private LocalDateTime createdAt;

    public String getCreatedTime() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime createdTime = this.createdAt;

        Duration duration = Duration.between(createdTime, now);
        long seconds = duration.getSeconds();

        if (seconds < 60) {
            return "Bây giờ";
        } else if (seconds < 3600) {
            long minutes = seconds / 60;
            return minutes + " phút trước";
        } else if (seconds < 86400) {
            long hours = seconds / 3600;
            return hours + " giờ trước";
        } else if (seconds < 604800) {
            long days = seconds / 86400;
            return days + " ngày trước";
        } else {
            return DateTimeFormatter.ofPattern("dd-MM-yyyy").format(createdTime);
        }
    }

    private LocalDateTime updatedAt;

    public String getUpdatedTime() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime updatedTime = this.updatedAt;

        Duration duration = Duration.between(updatedTime, now);
        long seconds = duration.getSeconds();

        if (seconds < 60) {
            return "Bây giờ";
        } else if (seconds < 3600) {
            long minutes = seconds / 60;
            return minutes + " phút trước";
        } else if (seconds < 86400) {
            long hours = seconds / 3600;
            return hours + " giờ trước";
        } else if (seconds < 604800) {
            long days = seconds / 86400;
            return days + " ngày trước";
        } else {
            return DateTimeFormatter.ofPattern("dd-MM-yyyy").format(updatedTime);
        }
    }

    public void addTag(Tag tag) {
        if (listTag == null) {
            listTag = new ArrayList<>();
        }
        listTag.add(tag);
    }

    public void addChapter(Chapter chapter) {
        if (chapterList == null) {
            chapterList = new ArrayList<>();
        }
        chapterList.add(chapter);
    }

    public String getStatus() {
        return (this.status != null && this.status != "")?status:"Đang cập nhật";
    }
}

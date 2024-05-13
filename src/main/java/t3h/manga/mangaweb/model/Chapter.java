package t3h.manga.mangaweb.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.Duration;

@Entity
@Data
@Getter
@Setter
@Table(name = "chapter")
public class Chapter {
    public Chapter() {
        this.createdAt = LocalDateTime.now();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private Long views;

    @Column(length = 1000)
    private String source;

    @ManyToOne
    @JoinColumn(name = "manga_id")
    private Manga manga;

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

    @Column(columnDefinition = "TEXT")
    private String pathImagesList;

}

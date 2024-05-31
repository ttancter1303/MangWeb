package t3h.manga.mangaweb.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "SavedManga")
public class SavedManga {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne
    @JoinColumn(name = "manga_id", nullable = false)
    private Manga manga;
}

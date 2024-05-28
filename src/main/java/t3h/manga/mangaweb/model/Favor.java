package t3h.manga.mangaweb.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Entity
@Data
@Getter
@Setter
public class Favor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    private Account user;

    @ManyToMany
    @JoinTable(
        name="favor_manga",
        joinColumns = @JoinColumn(name="favor_id"),
        inverseJoinColumns = @JoinColumn(name = "manga_id")
    )
    private List<Manga> listMangas;
}

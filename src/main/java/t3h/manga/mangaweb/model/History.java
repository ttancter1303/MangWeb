// package t3h.manga.mangaweb.model;

// import java.util.List;

// import jakarta.persistence.*;
// import lombok.Data;
// import lombok.Getter;
// import lombok.Setter;


// @Entity
// @Data
// @Getter
// @Setter
// public class History {
//     @Id
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     private Integer id;

//     @OneToOne
//     private Account user;

//     @ManyToMany
//     @JoinTable(
//         name="history_manga",
//         joinColumns = @JoinColumn(name="history_id"),
//         inverseJoinColumns = @JoinColumn(name = "manga_id")
//     )
//     private List<Manga> listMangas;

//     @Column(nullable = true)
//     private Chapter lastestChapter;
// }

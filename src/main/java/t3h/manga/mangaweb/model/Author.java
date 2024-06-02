 package t3h.manga.mangaweb.model;

 import jakarta.persistence.*;
 import lombok.Data;

 import java.util.ArrayList;
 import java.util.List;

 @Entity
 @Table(name = "Author")
 @Data
 public class Author {
     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Integer id;

     @Column(name = "AuthorName", nullable = false)
     private String name;

     @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
     private List<Manga> mangaList = new ArrayList<>();
     public Author(String authorName) {
     }

     public Author() {

     }


 }

package t3h.manga.mangaweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import t3h.manga.mangaweb.model.Manga;
import java.util.List;
import java.util.Optional;


public interface MangaRepository extends JpaRepository<Manga,Integer> {
    List<Manga> findMangaByName(String name);

    @Query("SELECT m FROM Manga m JOIN m.listTag t WHERE t.name = :tag")
    List<Manga> findByTagName(@Param("tag") String tag);

    @Query("SELECT m FROM Manga m WHERE m.name LIKE %:name%")
    List<Manga> findMangaByNameLike(@Param("name") String name);


    Optional<Manga> findBySource(String source);

    void deleteById(Integer id);

}

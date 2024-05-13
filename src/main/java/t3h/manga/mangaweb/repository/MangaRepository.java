package t3h.manga.mangaweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import t3h.manga.mangaweb.model.Manga;
import java.util.List;
import java.util.Optional;


public interface MangaRepository extends JpaRepository<Manga,Integer> {
    Manga findMangaByName(String name);
    
    Optional<Manga> findBySource(String source);
}

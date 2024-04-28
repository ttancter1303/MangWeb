package t3h.manga.mangaweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import t3h.manga.mangaweb.entity.Manga;

public interface MangaRepository extends JpaRepository<Manga,Integer> {

}

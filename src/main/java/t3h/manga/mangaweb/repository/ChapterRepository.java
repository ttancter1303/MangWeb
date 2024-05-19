package t3h.manga.mangaweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import t3h.manga.mangaweb.model.Chapter;

import java.util.List;

public interface ChapterRepository extends JpaRepository<Chapter,Integer> {
    Chapter findByName(String name);
    List<Chapter> findByMangaId(Integer id);
    int countByMangaId(Integer mangaId);
}

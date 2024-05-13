package t3h.manga.mangaweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import t3h.manga.mangaweb.model.Chapter;

public interface ChapterRepository extends JpaRepository<Chapter,Integer> {
    Chapter findByName(String name);
}

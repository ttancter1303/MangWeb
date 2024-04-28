package t3h.manga.mangaweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import t3h.manga.mangaweb.entity.Chapter;

public interface ChapterRepository extends JpaRepository<Chapter,Integer> {
}

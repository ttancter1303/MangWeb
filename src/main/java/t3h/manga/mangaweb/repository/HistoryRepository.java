package t3h.manga.mangaweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import t3h.manga.mangaweb.model.Account;
import t3h.manga.mangaweb.model.Manga;
import t3h.manga.mangaweb.model.History;

import java.util.List;

public interface HistoryRepository extends JpaRepository<History,Integer> {
    History findByUserAndManga(Account user, Manga manga);
    
    @Query("SELECT h FROM History h WHERE h.user = :user ORDER BY h.updatedAt DESC")
    List<History> findByUser(@Param("user") Account user);
    @Query("SELECT h.manga.id, COUNT(h) as readCount FROM History h GROUP BY h.manga.id ORDER BY readCount DESC")
    List<Object[]> findTopMangasByReadCount();
}

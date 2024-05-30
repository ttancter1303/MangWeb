package t3h.manga.mangaweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import t3h.manga.mangaweb.model.Account;
import t3h.manga.mangaweb.model.SavedManga;

import java.util.List;
import java.util.Optional;

public interface SavedMangaRepository extends JpaRepository<SavedManga, Integer> {
    Optional<SavedManga> findByAccountIdAndMangaId(Integer accountId, Integer mangaId);
    List<SavedManga> findByAccount(Account account);
}
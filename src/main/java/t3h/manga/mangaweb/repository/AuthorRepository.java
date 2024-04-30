package t3h.manga.mangaweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import t3h.manga.mangaweb.entity.Author;

public interface AuthorRepository extends JpaRepository<Author,Integer> {
    Author findAuthorByName(String name);
}

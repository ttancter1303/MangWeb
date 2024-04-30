package t3h.manga.mangaweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import t3h.manga.mangaweb.entity.Tag;

public interface TagRepository extends JpaRepository<Tag,Integer> {
    Tag findByName(String name);
}

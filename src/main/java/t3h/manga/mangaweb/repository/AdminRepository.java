package t3h.manga.mangaweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import t3h.manga.mangaweb.entity.Admin;

public interface AdminRepository extends JpaRepository<Admin,Integer> {
}

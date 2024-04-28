package t3h.manga.mangaweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import t3h.manga.mangaweb.entity.Account;

public interface AccountRepository extends JpaRepository<Account,Integer> {

}

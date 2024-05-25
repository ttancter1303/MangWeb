package t3h.manga.mangaweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import t3h.manga.mangaweb.model.Account;


public interface AccountRepository extends JpaRepository<Account,Integer> {
    @Query(value = "FROM Account WHERE username = :username")
    Account getDataByUsername(String username);
    Account findAccountByUsername(String username);
    Account findAccountByEmail(String email);
}

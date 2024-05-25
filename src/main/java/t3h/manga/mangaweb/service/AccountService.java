package t3h.manga.mangaweb.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import t3h.manga.mangaweb.model.Account;

import java.util.List;

public interface AccountService extends UserDetailsService {
        public List<Account> getAll();
}

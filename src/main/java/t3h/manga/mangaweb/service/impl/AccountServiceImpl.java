package t3h.manga.mangaweb.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import t3h.manga.mangaweb.model.Account;
import t3h.manga.mangaweb.repository.AccountRepository;
import t3h.manga.mangaweb.service.AccountService;

import java.util.Collections;
import java.util.List;
@Component
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountRepository accountRepository;
    @Override
    public List<Account> getAll() {
        List<Account> accounts = accountRepository.findAll();
        return accounts;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            Account account = accountRepository.getDataByUsername(username);
            if (account == null){
                throw new UsernameNotFoundException("Admin not found");
            }
            return new User(username, account.getPassword(), Collections.emptyList());

    }
}

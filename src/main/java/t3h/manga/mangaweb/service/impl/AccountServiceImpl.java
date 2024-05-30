package t3h.manga.mangaweb.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import t3h.manga.mangaweb.model.Account;
import t3h.manga.mangaweb.repository.AccountRepository;
import t3h.manga.mangaweb.service.AccountService;

import org.springframework.session.Session;
import org.springframework.session.SessionRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountRepository accountRepository;
    private Object Optional;

    @Override
    public List<Account> getAll() {
        List<Account> accounts = accountRepository.findAll();
        return accounts;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.getDataByUsername(username);
        if (account == null) {
            throw new UsernameNotFoundException("Admin not found");
        }
        List<GrantedAuthority> listRole = new ArrayList<>();
        listRole.add(new SimpleGrantedAuthority(account.getRole()));
        System.out.println(account);
        return new User(username, account.getPassword(), listRole);
    }
    public void changePassword(Account account, String newPassword) {
        account.setPassword(new BCryptPasswordEncoder().encode(newPassword));
        accountRepository.save(account);
    }

}

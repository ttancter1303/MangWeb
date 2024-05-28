package t3h.manga.mangaweb.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import t3h.manga.mangaweb.model.Account;
import t3h.manga.mangaweb.model.Manga;
import t3h.manga.mangaweb.repository.AccountRepository;
import t3h.manga.mangaweb.service.MangaService;

@Service
public class MangaServiceImpl implements MangaService
{
    @Autowired private t3h.manga.mangaweb.repository.MangaRepository mangaRepository;
    @Autowired
    AccountRepository accountRepository;

    @Override public List<Manga> getAllManga()
    {
        return mangaRepository.findAll();
    }

    @Override public Manga  getMangaById(int id)
    {

        Manga Manga = null;

        Optional<Manga> optional = mangaRepository.findById(id);

        if(optional.isPresent())
        {
            Manga = optional.get();
        }

        return Manga;

    }
//    @Override
//    public void saveMangaForUser(String username, Integer mangaId) {
//        Account user = accountRepository.findAccountByUsername(username);
//        Manga manga = mangaRepository.findById(mangaId).orElseThrow(() -> new RuntimeException("Manga not found"));
//        user.getSavedMangas().add(manga);
//        accountRepository.save(user);
//    }
//    @Override
//    public List<Manga> getSavedMangasForUser(String username) {
//        Account user = accountRepository.findAccountByUsername(username);
//        return user.getSavedMangas();
//    }
    @Override public Manga  getMangaBySrc(String src) 
    {

        Manga Manga = null;

        Optional<Manga> optional = mangaRepository.findBySource(src);

        if(optional.isPresent())
        {
            Manga = optional.get();
        }

        return Manga;

    }

    @SuppressWarnings("null")
    @Override
    public void saveManga(Manga Manga)
    {
        this.mangaRepository.save(Manga);
    }

    @Override
    public void deleteManga(int id)
    {
        this.mangaRepository.deleteById(id);
    }
}

package t3h.manga.mangaweb.service;

import t3h.manga.mangaweb.model.Account;
import t3h.manga.mangaweb.model.Manga;
import t3h.manga.mangaweb.model.SavedManga;
import t3h.manga.mangaweb.repository.SavedMangaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SavedMangaService {

    @Autowired
    private SavedMangaRepository savedMangaRepository;

    public String saveManga(Account account, Manga manga) {
        Optional<SavedManga> existingSave = savedMangaRepository.findByAccountIdAndMangaId(account.getId(), manga.getId());
        if (existingSave.isPresent()) {
            return "Manga already saved!";
        }

        SavedManga savedManga = new SavedManga();
        savedManga.setAccount(account);
        savedManga.setManga(manga);
        savedMangaRepository.save(savedManga);
        return "Manga saved successfully!";
    }
    public List<Manga> getSavedMangas(Account account) {
        return savedMangaRepository.findByAccount(account)
                .stream()
                .map(SavedManga::getManga)
                .collect(Collectors.toList());
    }
}

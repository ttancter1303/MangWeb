package t3h.manga.mangaweb.service;

import java.util.List;

import t3h.manga.mangaweb.model.Manga;

public interface MangaService 
{
    List<Manga> getAllManga();
    void saveMangaForUser(String username, Integer mangaId);
    List<Manga> getSavedMangasForUser(String username);
    Manga getMangaById(int id);

    Manga getMangaBySrc(String src);

    void saveManga(Manga dl);

    void deleteManga(int id);
}
package t3h.manga.mangaweb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import t3h.manga.mangaweb.entity.Chapter;
import t3h.manga.mangaweb.entity.Manga;
import t3h.manga.mangaweb.entity.Tag;
import t3h.manga.mangaweb.repository.ChapterRepository;
import t3h.manga.mangaweb.repository.MangaRepository;
import t3h.manga.mangaweb.repository.TagRepository;

import java.util.List;

@Service
public class MangaService {
    @Autowired
    private MangaRepository mangaRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private ChapterRepository chapterRepository;
    @Transactional
    public Manga saveMangaWithTagsAndChapters(Manga manga, List<Tag> tags, List<Chapter> chapters) {
        for (Tag tag : tags) {
            manga.getListTag().add(tag);
            tag.getMangas().add(manga);
            tagRepository.save(tag);
        }

        for (Chapter chapter : chapters) {
            chapter.setManga(manga);
            chapterRepository.save(chapter);
        }

        return mangaRepository.save(manga);
    }
    @Transactional
    public Manga saveMangaWithTags(Manga manga, List<Tag> tags) {
        for (Tag tag : tags) {
            manga.getListTag().add(tag);
            tag.getMangas().add(manga);
        }
        return mangaRepository.save(manga);
    }
}

package t3h.manga.mangaweb.service;

import java.util.List;
import t3h.manga.mangaweb.model.Tag;

public interface TagService 
{
    List<Tag> getAllTags();

    Tag getTagById(int id);

    void saveTag(Tag dl);

    void deleteTag(int id);
}
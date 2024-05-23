package t3h.manga.mangaweb.service.impl;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import t3h.manga.mangaweb.model.Tag;
import t3h.manga.mangaweb.service.TagService;

@Service
public class TagServiceImpl implements TagService
{
    @Autowired private t3h.manga.mangaweb.repository.TagRepository TagRepository;

    @Override public List<Tag> getAllTags()
    {
        return TagRepository.findAll();
    }

    @Override public Tag  getTagById(int id) 
    {

        Tag Tag = null;

        Optional<Tag> optional = TagRepository.findById(id);

        if(optional.isPresent())
        {
            Tag = optional.get();
        } else{
            throw new RuntimeException("Cannot find your Tag!");
        }

        return Tag;

    }

    @Override
    public void saveTag(Tag Tag)
    {
        this.TagRepository.save(Tag);
    }

    @Override
    public void deleteTag(int id)
    {
        this.TagRepository.deleteById(id);
    }
}


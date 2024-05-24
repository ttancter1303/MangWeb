package t3h.manga.mangaweb.service;

import org.springframework.stereotype.Service;

import t3h.manga.mangaweb.components.helper.SlugUtil;

@Service
public class SlugService {

    public String convertToSlug(String input) {
        return SlugUtil.toSlug(input);
    }
}

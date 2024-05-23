package t3h.manga.mangaweb.controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.context.request.WebRequest;

import t3h.manga.mangaweb.model.Tag;
import t3h.manga.mangaweb.service.TagService;

@ControllerAdvice
public class GlobalController {

    @Autowired
    private TagService tagService;

    @ModelAttribute("global")
    public HashMap<String, Object> getGlobalData() {
        HashMap<String, Object> global = new HashMap<>();
        global.put("tags", tagService.getAllTags());
        global.put("appname", "NetSteal");
        return global;
    }
}

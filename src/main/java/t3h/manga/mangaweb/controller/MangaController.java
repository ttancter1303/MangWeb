//package t3h.manga.mangaweb.controller;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import t3h.manga.mangaweb.entity.Manga;
//import t3h.manga.mangaweb.repository.MangaRepository;
//
//import java.util.List;
//import java.util.Optional;
//
//@RestController
//@RequestMapping("/api/manga")
//public class MangaController {
//
//    @Autowired
//    private MangaRepository mangaRepository;
//
//    // GET all mangas
//    @GetMapping
//    public ResponseEntity<List<Manga>> getAllMangas() {
//        List<Manga> mangas = mangaRepository.findAll();
//        return new ResponseEntity<>(mangas, HttpStatus.OK);
//    }
//
//    // GET a manga by ID
//    @GetMapping("/{id}")
//    public ResponseEntity<Manga> getMangaById(@PathVariable("id") Integer id) {
//        Optional<Manga> mangaOptional = mangaRepository.findById(id);
//        return mangaOptional.map(manga -> new ResponseEntity<>(manga, HttpStatus.OK))
//                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
//    }
//
//    // POST create a new manga
//    @PostMapping
//    public ResponseEntity<Manga> createManga(@RequestBody Manga manga) {
//        Manga createdManga = mangaRepository.save(manga);
//        return new ResponseEntity<>(createdManga, HttpStatus.CREATED);
//    }
//
//    // PUT update an existing manga
//    @PutMapping("/{id}")
//    public ResponseEntity<Manga> updateManga(@PathVariable("id") Integer id, @RequestBody Manga manga) {
//        Optional<Manga> mangaOptional = mangaRepository.findById(id);
//        if (mangaOptional.isPresent()) {
//            manga.setMangaID(id); // Set ID của manga trước khi cập nhật
//            Manga updatedManga = mangaRepository.save(manga);
//            return new ResponseEntity<>(updatedManga, HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }
//
//    // DELETE delete a manga
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteManga(@PathVariable("id") Integer id) {
//        Optional<Manga> mangaOptional = mangaRepository.findById(id);
//        if (mangaOptional.isPresent()) {
//            mangaRepository.deleteById(id);
//            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//        } else {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }
//}
//

package t3h.manga.mangaweb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import t3h.manga.mangaweb.model.Manga;
import t3h.manga.mangaweb.repository.HistoryRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MangaRankingService {

    @Autowired
    private HistoryRepository historyRepository;

    public List<MangaRankingDTO> getTopRankedMangas() {
        List<Object[]> results = historyRepository.findTopMangasByReadCount();
        return results.stream()
                .map(result -> new MangaRankingDTO((Integer) result[0], (Long) result[1]))
                .collect(Collectors.toList());
    }

    public static class MangaRankingDTO {
        private Integer mangaId;
        private Long readCount;

        public MangaRankingDTO(Integer mangaId, Long readCount) {
            this.mangaId = mangaId;
            this.readCount = readCount;
        }

        // Getters and Setters
        public Integer getMangaId() {
            return mangaId;
        }

        public void setMangaId(Integer mangaId) {
            this.mangaId = mangaId;
        }

        public Long getReadCount() {
            return readCount;
        }

        public void setReadCount(Long readCount) {
            this.readCount = readCount;
        }
    }
}

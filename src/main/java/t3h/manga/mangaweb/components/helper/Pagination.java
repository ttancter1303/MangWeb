package t3h.manga.mangaweb.components.helper;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Pagination {
    public Pagination(long total, Integer perPage, Integer current) {
        this.totalPage = (total % perPage != 0)? (total / perPage + 1) : (total / perPage);
        this.current = current;
        this.last = this.totalPage;
        this.first = 1;
        this.prev = current - 1;
        this.next = current + 1;
    }

    private long totalPage;
    private Integer current;
    private long last;
    private Integer first;
    private Integer prev;
    private Integer next;

}

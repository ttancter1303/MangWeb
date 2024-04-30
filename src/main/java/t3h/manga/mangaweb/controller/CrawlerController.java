package t3h.manga.mangaweb.controller;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import t3h.manga.mangaweb.crawler.MangaDetailCrawler;
import t3h.manga.mangaweb.entity.Author;

import java.util.List;

@RestController
@RequestMapping("/crawler")
public class CrawlerController {
    @GetMapping()
    public ResponseEntity crawler(){
        SessionFactory sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();

        // Mở một phiên làm việc (session)
        try (Session session = sessionFactory.openSession()) {
            // Bắt đầu giao dịch
            session.beginTransaction();

            // Thực hiện truy vấn (ví dụ: lấy danh sách tất cả các tác giả)
            List<Author> authors = session.createQuery("FROM Author", Author.class).getResultList();
            MangaDetailCrawler mangaDetailCrawler = new MangaDetailCrawler();
            mangaDetailCrawler.mangaCrawler();
            // In ra thông tin các tác giả
            for (Author author : authors) {
                System.out.println("Author ID: " + author.getAuthorID());
                System.out.println("Author Name: " + author.getName());
            }

            // Kết thúc giao dịch
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Đóng SessionFactory khi không cần sử dụng nữa
            sessionFactory.close();
        }
        return new ResponseEntity("Crawler done", HttpStatus.OK);
    }
}

package org.example;
import org.example.model.Author;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Khởi tạo SessionFactory từ cấu hình
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
    }
}


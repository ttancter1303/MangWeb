package t3h.manga.mangaweb.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String fullName;
    @Column(unique = true, length = 20)
    private String username;
    private String password;
    private Date createdAt;
    private Date updateAt;
    @Column(unique = true)
    private String Email;
}

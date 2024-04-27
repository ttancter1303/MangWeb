package org.example.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Author")
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AuthorID")
    private long authorID;
    @Column(name = "AuthorName", nullable = false)
    private String name;

    public long getAuthorID() {
        return authorID;
    }

    public Author(String name) {
        this.name = name;
    }

    public void setAuthorID(long authorID) {
        this.authorID = authorID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Author(long authorID, String name) {
        this.authorID = authorID;
        this.name = name;
    }
}

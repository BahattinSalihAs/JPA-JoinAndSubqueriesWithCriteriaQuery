package org.example.entities;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    @ManyToMany(mappedBy = "booksList")
    private List<Author> authorsList;
    @ManyToMany(mappedBy = "booksList")
    private List<BookShop> bookShopsList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Author> getAuthorsList() {
        return authorsList;
    }

    public void setAuthorsList(List<Author> authorsList) {
        this.authorsList = authorsList;
    }

    public List<BookShop> getBookShopsList() {
        return bookShopsList;
    }

    public void setBookShopsList(List<BookShop> bookShopsList) {
        this.bookShopsList = bookShopsList;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                '}';
    }
}

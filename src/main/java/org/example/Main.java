package org.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.example.entities.Author;
import org.example.entities.Book;
import org.example.entities.BookShop;
import org.example.persistence.CustomPersistenceUnitInfo;
import org.hibernate.jpa.HibernatePersistenceProvider;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        String puName = "pu-name";
        Map<String,String> props = new HashMap<>();
        props.put("hibernate.show_sql","true");
        props.put("hibernate.hbm2ddl.auto","none");
        EntityManagerFactory emf = new HibernatePersistenceProvider()
                .createContainerEntityManagerFactory(new CustomPersistenceUnitInfo(puName),props);
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();
            /*
            Author a = new Author();
            Author a2 = new Author();
            Author a3 = new Author();
            Book b = new Book();
            Book b2 = new Book();
            Book b3 = new Book();
            Book b4 = new Book();
            Book b5 = new Book();
            BookShop bs = new BookShop();
            BookShop bs2 = new BookShop();
            BookShop bs3 = new BookShop();

            a.setName("Bahattin Salih As");
            a2.setName("Atilla Yılmaz");
            a3.setName("Hans Zimmer");
            b.setTitle("Bunları Kimseye Anlatamamıştım");
            b2.setTitle("Bir Çocuğun Günlüğü");
            b3.setTitle("What is the writer code?");
            b4.setTitle("Help Me");
            b5.setTitle("Bu kitabı okuma");

            bs.setName("Kitap.com");
            bs2.setName("Nadir Kitap");
            bs3.setName("D&R");

            a.setBooksList(List.of(b));
            a2.setBooksList(List.of(b2,b5));
            a3.setBooksList(List.of(b3,b4));

            b.setAuthorsList(List.of(a));
            b2.setAuthorsList(List.of(a2));
            b3.setAuthorsList(List.of(a3));
            b4.setAuthorsList(List.of(a3));
            b5.setAuthorsList(List.of(a2));

            b.setBookShopsList(List.of(bs,bs2));
            b2.setBookShopsList(List.of(bs2,bs3));
            b3.setBookShopsList(List.of(bs2));
            b4.setBookShopsList(List.of(bs3));
            b5.setBookShopsList(List.of(bs,bs2,bs3));

            bs.setBooksList(List.of(b,b5));
            bs2.setBooksList(List.of(b,b2,b3,b5));
            bs3.setBooksList(List.of(b2,b4,b5));

            em.persist(a);
            em.persist(a2);
            em.persist(a3);
            em.persist(b);
            em.persist(b2);
            em.persist(b3);
            em.persist(b4);
            em.persist(b5);
            em.persist(bs);
            em.persist(bs2);
            em.persist(bs3);
            */
            CriteriaBuilder builder = em.getCriteriaBuilder();
            CriteriaQuery<Tuple> cq = builder.createTupleQuery();

            Root<Book> bookRoot = cq.from(Book.class);
            Join<Book, Author> joinAuthor = bookRoot.join("authorsList", JoinType.LEFT);
            Join<Book, BookShop> joinBookShop = bookRoot.join("bookShopsList", JoinType.LEFT);

            cq.multiselect(bookRoot,joinAuthor, joinBookShop);

            TypedQuery<Tuple> q = em.createQuery(cq);
            q.getResultList().forEach(t -> System.out.println(t.get(0) + " " + t.get(1) + " " + t.get(2)));

            CriteriaQuery<Tuple> cq2 = builder.createTupleQuery();
            Root<Book> authorBook = cq2.from(Book.class);
            Join<Book, Author> joinAuthor2 = authorBook.join("authorsList", JoinType.LEFT);

            cq2.multiselect(joinAuthor2.get("name"), builder.count(joinAuthor2.get("name")));
            cq2.where(builder.isNotNull(joinAuthor2.get("name")));
            cq2.groupBy(joinAuthor2.get("name"));
            cq2.orderBy(builder.desc(builder.count(joinAuthor2.get("name"))));

            TypedQuery<Tuple> q2 = em.createQuery(cq2);
            q2.getResultList().forEach(o -> System.out.println(o.get(0) + " yazarının " + o.get(1) + " adet kitabı vardır."));

            em.getTransaction().commit();
        }finally {
            em.close();
        }
    }
}
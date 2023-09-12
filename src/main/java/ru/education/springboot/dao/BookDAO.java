package ru.education.springboot.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.education.springboot.models.Book;
import ru.education.springboot.models.Person;

import java.util.List;
import java.util.Optional;

@Component
public class BookDAO {
    private final JdbcTemplate jdbcTemplate;
    private final SessionFactory sessionFactory;

    @Autowired
    public BookDAO(JdbcTemplate jdbcTemplate, SessionFactory sessionFactory) {
        this.jdbcTemplate = jdbcTemplate;
        this.sessionFactory = sessionFactory;
    }

    @Transactional
    public List<Book> index() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("select b from Book b", Book.class).getResultList();
    }

    @Transactional
    public Book show(int id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(Book.class, id);
    }

    @Transactional
    public void create(Book book) {
        Session session = sessionFactory.getCurrentSession();
        session.save(book);

    }

    @Transactional
    public void update(int id, Book bookToUpdate) {
        Session session = sessionFactory.getCurrentSession();

        Book book = session.get(Book.class, id);

        book.setTitle(bookToUpdate.getTitle());
        book.setAuthor((bookToUpdate.getAuthor()));
        book.setAge(bookToUpdate.getAge());

        session.save(book);

    }

    @Transactional
    public void deleteBook(int id) {
        Session session = sessionFactory.getCurrentSession();
        session.delete(id);
    }

    @Transactional
    public Optional<Person> getBookOwner(int id) {
        Session session = sessionFactory.getCurrentSession();

        Book book = session.get(Book.class, id);

        return Optional.ofNullable(book.getOwner());

    }

    @Transactional
    public void release(int id) {
        Session session = sessionFactory.getCurrentSession();

        Book book = session.get(Book.class, id);

        Person person = book.getOwner();
        person.getBookList().remove(book);

        book.setOwner(null);

    }

    @Transactional
    public void assign(int id, Person selectedPerson) {
        Session session = sessionFactory.getCurrentSession();

        Book book = session.get(Book.class, id);

        Person person = session.get(Person.class, selectedPerson.getId());

        person.getBookList().add(book);

        book.setOwner(person);

    }
}

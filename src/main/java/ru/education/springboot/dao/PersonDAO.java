package ru.education.springboot.dao;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.education.springboot.models.Book;
import ru.education.springboot.models.Person;
import ru.education.springboot.models.PersonRowMapper;

import javax.persistence.EntityManager;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
public class PersonDAO {

    private final JdbcTemplate jdbcTemplate;
    private final EntityManager entityManager;

    @Autowired
    public PersonDAO(JdbcTemplate jdbcTemplate, EntityManager entityManager) {
        this.jdbcTemplate = jdbcTemplate;
        this.entityManager = entityManager;
    }

    @Transactional(readOnly = true)
    public List<Person> index() {
        Session session = entityManager.unwrap(Session.class);;
        // Тут и будет обычный Hibernate код
        return session.createQuery("select p from Person p", Person.class)
                .getResultList();
    }

    @Transactional(readOnly = true)
    public Person show(int id) {
        Session session = entityManager.unwrap(Session.class);;
        return session.get(Person.class, id);
    }

    public Optional<Person> show(String name) {
        return jdbcTemplate.query("SELECT * FROM PERSON WHERE name=?", new Object[]{name},
                new PersonRowMapper()).stream().findAny();
    }
    @Transactional
    public void createPerson(Person person) {
        Session session = entityManager.unwrap(Session.class);;
        session.save(person);

    }

    @Transactional
    public void updatePerson(int id, Person updatePerson) {
        Session session = entityManager.unwrap(Session.class);;

        Person person = session.get(Person.class, id);

        person.setFullName(updatePerson.getFullName());
        person.setYearOfBirth(updatePerson.getYearOfBirth());
    }
    @Transactional
    public void deletePerson(int id) {
        Session session = entityManager.unwrap(Session.class);;
        session.remove(session.get(Person.class, id));
    }

    @Transactional
    public List<Book> getBooksByPersonId(int id) {
        Session session = entityManager.unwrap(Session.class);

        Person person = session.get(Person.class, id);

        Hibernate.initialize(person.getBookList());

        return person.getBookList();

    }

    @Transactional(readOnly = true)
    public Person getPersonByIdLazyLoad(int id) {
        Session session = entityManager.unwrap(Session.class);

        // создаем новый обект - ему надо назначить владельца
        Book book = new Book();

        // нам нужен этот объект только для выстраивания новых связей
        // значения полей этого человека не нужны
        Person person = session.load(Person.class, id);

        // в колонку внешнего ключа будет положено значние Id текущего человека
        // то что человек существует будет проверять БД.
        book.setOwner(person);
        return person;
    }

    @Transactional(readOnly = true)
    public void test() {
        // достаем session из entityManager (из обертки)
        Session session = entityManager.unwrap(Session.class);
        // SQL : A LEFT JOIN B -> результрующая объединенная таблица
//        List<Person> list = session.createQuery("select p from Person p left join fetch p.bookList")
//                .getResultList();

        // чтобы избавиться от дублей внесем резлуьтат запроса в HashSet()
        Set<Person> list = new HashSet<Person> (session.createQuery("select p from Person p left join fetch p.bookList")
                .getResultList());


        for(Person person : list)
            System.out.println("Person : " + person.getFullName() + ", has : " + person.getBookList());
    }
}

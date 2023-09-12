package ru.education.springboot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.education.springboot.models.Book;
import ru.education.springboot.models.Person;


import java.util.List;

@Repository
public interface BooksRepository extends JpaRepository<Book, Integer> {

    List<Book> findByTitle(String title);

    List<Book> findByAuthor(String author);

    List<Book> findByOwner(Person owner);

    List<Book> findByTitleStartingWith(String startingWith);
}

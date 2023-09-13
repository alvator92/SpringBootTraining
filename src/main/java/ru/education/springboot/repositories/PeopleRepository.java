package ru.education.springboot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.education.springboot.models.Book;
import ru.education.springboot.models.Person;

import java.util.List;
import java.util.Optional;

@Repository
public interface PeopleRepository extends JpaRepository<Person, Integer> {
    Optional<Person> findByFullName(String name);

    List<Person> findByFullNameOrderByYearOfBirth(String name);

    List<Person> findByFullNameStartingWith(String startingWith);

    Person findByBookList(Book book);

}

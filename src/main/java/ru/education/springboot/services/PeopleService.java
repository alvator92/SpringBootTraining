package ru.education.springboot.services;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.education.springboot.models.Book;
import ru.education.springboot.models.Mood;
import ru.education.springboot.models.Person;
import ru.education.springboot.repositories.PeopleRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PeopleService {

    private final PeopleRepository peopleRepository;

    @Autowired
    public PeopleService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    public List<Person> findAll() {
        return peopleRepository.findAll();
    }

    public Person findOne(int id) {
        Optional<Person> person = peopleRepository.findById(id);
        return person.orElse(null);
    }

    public Person findOneWithList(int id) {
        Optional<Person> person = peopleRepository.findById(id);
        Hibernate.initialize(person.get().getBookList());
        return person.orElse(null);
    }

    @Transactional
    public void save(Person person) {
        person.setCreatedAt(new Date());
        person.setMood(Mood.CALM);
        peopleRepository.save(person);
    }

    @Transactional
    public void update(int id, Person personToBeUpdate) {
        personToBeUpdate.setCreatedAt(new Date());
        personToBeUpdate.setId(id);
        peopleRepository.save(personToBeUpdate);
    }

    @Transactional
    public void delete(int id) {
        peopleRepository.deleteById(id);
    }

    @Transactional
    public Person findPersonByBookList(Book book) {
        return peopleRepository.findByBookList(book);

    }

//    @Transactional
//    public void assignBookToPerson(Book book, Person person) {
//        person.getBookList().add(book);
//        save(person);
//    }
}

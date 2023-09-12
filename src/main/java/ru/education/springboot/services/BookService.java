package ru.education.springboot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.education.springboot.models.Book;
import ru.education.springboot.models.Person;
import ru.education.springboot.repositories.BooksRepository;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BookService {

    private final BooksRepository booksRepository;

    @Autowired
    public BookService(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

    public List<Book> findByBookTitle(String title) {
        return booksRepository.findByTitle(title);
    }

    public List<Book> findByBookAuthor(String author) {
        return booksRepository.findByAuthor(author);
    }

    public List<Book> findBookByOwner(Person owner) {
        return booksRepository.findByOwner(owner);
    }

    public List<Book> findAll() {
        return booksRepository.findAll();
    }

    public Page<Book> findAll(Pageable pageable) {
        return booksRepository.findAll((org.springframework.data.domain.Pageable) pageable);
    }

    public Page<Book> findAllWithPagination(int page, int booksPerPage) {
        return booksRepository.findAll(PageRequest.of(page, booksPerPage));
    }

    public List<Book> findAllOrderByAge() {
        return booksRepository.findAll(Sort.by("age"));
    }

    public Page<Book> findAllWithPaginationOrderByAge(int page, int booksPerPage) {
        return booksRepository.findAll(PageRequest.of(page, booksPerPage, Sort.by("age")));
    }

    public Book findOne(int id) {
        Optional<Book> book = booksRepository.findById(id);
        return book.orElse(null);
    }

    @Transactional
    public void save(Book book) {
        booksRepository.save(book);
    }

    @Transactional
    public void update(int id, Book book) {
        book.setId(id);
        booksRepository.save(book);
    }

    @Transactional
    public void delete(int id) {
        booksRepository.deleteById(id);
    }

    public List<Book> findByTitleStartingWith(String startingWith) {
        return booksRepository.findByTitleStartingWith(startingWith);
    }
}

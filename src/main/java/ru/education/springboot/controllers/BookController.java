package ru.education.springboot.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.education.springboot.dao.BookDAO;
import ru.education.springboot.dao.PersonDAO;
import ru.education.springboot.models.Book;
import ru.education.springboot.models.Person;
import ru.education.springboot.services.BookService;
import ru.education.springboot.services.PeopleService;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("/books")
public class BookController {

    private final BookDAO bookDAO;
    private final PersonDAO personDAO;
    private final BookService bookService;
    private final PeopleService peopleService;


    @Autowired
    public BookController(BookDAO bookDAO, PersonDAO personDAO, BookService bookService, PeopleService peopleService) {
        this.bookDAO = bookDAO;
        this.personDAO = personDAO;
        this.bookService = bookService;
        this.peopleService = peopleService;
    }

    @GetMapping()
    public String index(
            Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int books_per_page,
            @RequestParam(defaultValue = "false") boolean sort_by_year) {
        Page<Book> books = null;


        if (sort_by_year) {
            books = bookService.findAllWithPaginationOrderByAge(page, books_per_page);
        }
        else {
            books = bookService.findAllWithPagination(page, books_per_page);
        }

        books.getContent();
        model.addAttribute("books", books);
        model.addAttribute("sort", sort_by_year);

        return "/books/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id")int id, Model model) {

        model.addAttribute("book", bookService.findOne(id));

//        Optional<Person> bookOwner = bookDAO.getBookOwner(id);
        Book book = bookService.findOne(id);
        Optional<Person> bookOwner = Optional.ofNullable(book.getOwner());

        if (bookOwner.isPresent()) {
            // проверка просрочки книги
            Person person = bookOwner.get();
            model.addAttribute("owner", person);
        }
        else
            model.addAttribute("people", peopleService.findAll());

        return "books/show";
    }

    @GetMapping("/new")
    public String newBook(@ModelAttribute("book")Book book) {
        return "books/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("book")@Valid Book book,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "books/new";

        bookService.save(book);

        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id,
                       Model model) {
        model.addAttribute("book", bookService.findOne(id));
        return "books/edit";

    }

    @PatchMapping("/{id}")
    public String update(@PathVariable("id") int id, @ModelAttribute("book") Book book, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "books/edit";

        bookService.update(id, book);

        return "redirect:/books";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        bookService.delete(id);
        return "redirect:/books";
    }

    @PatchMapping("/{id}/release")
    public String release(@PathVariable("id") int id) {
        Book book = bookService.findOne(id);
        book.setOwner(null);
        bookService.update(id, book);
//        Person person = peopleService.findPersonByBookList(book);

        return "redirect:/books/" + id;
    }

    @PostMapping("/{id}/assign")
    public String assign(@PathVariable("id") int id, @ModelAttribute("person") Person selectedPerson) {
        Book book = bookService.findOne(id);
        Person person = peopleService.findOneWithList(selectedPerson.getId());
        person.setCreatedAt(new Date());
        book.setOwner(person);
        book.setDateOfRent(new Date());
        bookService.update(id, book);

        return "redirect:/books/" + id;
    }

    @GetMapping("/search")
    public String search(Model model,
                         @RequestParam(defaultValue = "null") String keyword) {

        List<Book> books = null;

        if (keyword.equals("null"))
            return "books/search";

        else {
            books = bookService.findByTitleStartingWith(keyword);
            if (!books.isEmpty())
                model.addAttribute("books", books);
            else
                model.addAttribute("booksNotFound", "Книга не найдена");
        }

        return "books/search";
    }
}


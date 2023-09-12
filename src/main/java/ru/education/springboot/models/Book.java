package ru.education.springboot.models;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "Book")
public class Book {
    @Id
    @Column(name = "book_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "title")
    @NotEmpty(message = "У книги должно быть название")
    @Size(min = 2, max = 100, message = "Название должно быть в пределах от 2 до 100 символов")
    private String title;

    @Column(name = "author")
    @NotEmpty(message = "У книги Должен быть автор")
    @Size(min = 2, max = 100, message = "Имя автора должно быть")
    private String author;

    @Column(name = "age")
    @Min(value = 1500)
    private int age;

    @Column(name = "date_of_rent")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateOfRent;

    @Transient
    private boolean expired;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    private Person owner;

    public Book(String book, String author, int age) {
        this.title = book;
        this.author = author;
        this.age = age;
    }

    public Book() {
    }

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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Person getOwner() {
        return owner;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }

    public Date getDateOfRent() {
        return dateOfRent;
    }

    public void setDateOfRent(Date dateOfRent) {
        this.dateOfRent = dateOfRent;
    }

    public boolean isExpired() {
        Calendar calendar = Calendar.getInstance();
        if (this.getDateOfRent() == null)
            return false;
        calendar.setTimeInMillis(getDateOfRent().getTime());
        // 10 minutes expiration time
        calendar.add(calendar.MINUTE, 10);
        if (calendar.getTime().compareTo(new Date()) > 0 )
            return false;
        return true;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return age == book.age &&
                Objects.equals(title, book.title) &&
                Objects.equals(author, book.author) &&
                Objects.equals(owner, book.owner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, author, age, owner);
    }

    @Override
    public String toString() {
        return "Book = {" +
                title + ", " + author + ", " + age + "}";
    }
}


package ru.education.springboot.models;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "Person" +
        "")
public class Person {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    @NotEmpty(message = "Name should not be Empty")
    @Size(min = 2, max = 30, message = "Name should be beetween 2 to 30 characters")
    private String fullName;

    @Column(name = "age")
    @Min(value = 0, message = "Age should be greater than 0")
    private int yearOfBirth;

    @Column(name = "date_of_birth")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date dateOfBirth;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Enumerated(EnumType.STRING)
    private Mood mood;

    @Transient
    private boolean expired;

    @OneToMany(mappedBy = "owner",fetch = FetchType.LAZY)
    private List<Book> bookList;

    public Person(String name, int age) {
        this.fullName = name;
        this.yearOfBirth = age;
    }

    public Person() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getYearOfBirth() {
        return yearOfBirth;
    }

    public void setYearOfBirth(int yearOfBirth) {
        this.yearOfBirth = yearOfBirth;
    }

    public List<Book> getBookList() {
        return bookList;
    }

    public void setBookList(List<Book> bookList) {
        this.bookList = bookList;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Mood getMood() {
        return mood;
    }

    public void setMood(Mood mood) {
        this.mood = mood;
    }

    public boolean isExpired() {
        Calendar calendar = Calendar.getInstance();
        if (this.getCreatedAt() == null)
            return false;
        calendar.setTimeInMillis(getCreatedAt().getTime());
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
        Person that = (Person) o;
        return Objects.equals(this.fullName, that.fullName) &&
                Objects.equals(this.dateOfBirth, that.dateOfBirth) &&
                Objects.equals(this.createdAt, that.createdAt) &&
                this.mood == that.mood &&
                this.bookList.equals(that.bookList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fullName, yearOfBirth, dateOfBirth, createdAt, bookList);
    }
}

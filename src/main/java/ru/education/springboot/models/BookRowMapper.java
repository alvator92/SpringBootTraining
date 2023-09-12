package ru.education.springboot.models;


import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BookRowMapper implements RowMapper<Book> {

    @Override
    public Book mapRow(ResultSet resultSet, int i) throws SQLException {
        Book book = new Book();

        book.setId(resultSet.getInt("book_id"));
        book.setTitle(resultSet.getString("book"));
        book.setAuthor(resultSet.getString("author"));
        book.setAge(resultSet.getInt("age"));

        return book;
    }
}
package com.library.book.services.impl;

import java.util.ArrayList;
import java.util.List;

import com.github.javafaker.Faker;
import com.library.book.domain.Book;
import com.library.book.domain.BookEntity;

public final class TestData {

    private static final Faker faker = new Faker();

    private TestData() {}

    public static Book getBook() {
        return Book.builder()
                .isbn(faker.number().digits(9))
                .author(faker.book().author())
                .title(faker.book().title())
                .build();
    }

    public static BookEntity getBookEntity() {
        return BookEntity.builder()
                .isbn(faker.number().digits(9))
                .author(faker.book().author())
                .title(faker.book().title())
                .build();
    }
    public static List<BookEntity> getListOfBookEntity(int size) {
        final List<BookEntity> listOfBookEntity= new ArrayList<>();
        for (int i = 0; i < size; i++) {
            listOfBookEntity.add(getBookEntity());
        }
        return listOfBookEntity;
    }

    public static List<Book> getListOfBook(int size) {
        final List<Book> listOfBook= new ArrayList<>();
        for (int i = 0; i < size; i++) {
            listOfBook.add(getBook());
        }
        return listOfBook;
    }

    public BookEntity bookToBookEntity(final Book book) {
        return BookEntity.builder()
                .isbn(book.getIsbn())
                .author(book.getAuthor())
                .title(book.getTitle())
                .build();
    }

    public Book bookEntityToBook(final BookEntity bookEntity) {
        return Book.builder()
                .isbn(bookEntity.getIsbn())
                .author(bookEntity.getAuthor())
                .title(bookEntity.getTitle())
                .build();
    }
    
}

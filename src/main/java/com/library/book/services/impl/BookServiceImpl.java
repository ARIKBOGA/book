package com.library.book.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.library.book.domain.Book;
import com.library.book.domain.BookEntity;
import com.library.book.repositories.BookRepository;
import com.library.book.services.BookService;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    @Autowired
    public BookServiceImpl(final BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Book create(final Book book) {
        final BookEntity bookEntity = bookToBookEntity(book);
        final BookEntity savedBook = bookRepository.save(bookEntity);
        return bookEntityToBook(savedBook);
    }

    @Override
    public Optional<Book> findBookByID(String isbn) {
        return bookRepository
            .findById(isbn)
            .map(book -> bookEntityToBook(book)); // Same function, different usage in the map method
    }

    @Override
    public List<Book> findAllBooks(){
        return bookRepository
            .findAll()
            .stream()
            .map(this::bookEntityToBook)    // Same function, different usage in the map method. 
                                            // One can call the static methods of the class like this "::"
            .toList();
    }

    private BookEntity bookToBookEntity(final Book book) {
        return BookEntity.builder()
                .isbn(book.getIsbn())
                .author(book.getAuthor())
                .title(book.getTitle())
                .build();
    }

    private Book bookEntityToBook(final BookEntity bookEntity) {
        return Book.builder()
                .isbn(bookEntity.getIsbn())
                .author(bookEntity.getAuthor())
                .title(bookEntity.getTitle())
                .build();
    }
}

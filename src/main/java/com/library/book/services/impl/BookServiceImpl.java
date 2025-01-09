package com.library.book.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.introspect.TypeResolutionContext.Empty;
import com.library.book.domain.Book;
import com.library.book.domain.BookEntity;
import com.library.book.repositories.BookRepository;
import com.library.book.services.BookService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    public BookServiceImpl(final BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public boolean isBookExists(final Book book){
        return bookRepository.existsById(book.getIsbn());
    }

    @Override
    public Book save(final Book book) {
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

    @Override
    public void deleteBookByID(String isbn) {
        try{
            bookRepository.deleteById(isbn);
        }catch(EmptyResultDataAccessException e){
            log.debug("Attemtting to delete a book that does not exist", e);
        }
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

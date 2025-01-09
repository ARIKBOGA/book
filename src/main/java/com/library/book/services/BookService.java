package com.library.book.services;

import java.util.List;
import java.util.Optional;

import com.library.book.domain.Book;

public interface BookService {
    
    boolean isBookExists(Book book);

    Book save (Book book);

    Optional<Book> findBookByID(String isbn);

    List<Book> findAllBooks();

    void deleteBookByID(String isbn);
}

package com.library.book.controllers;

import com.library.book.domain.Book;
import com.library.book.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(final BookService bookService) {
        this.bookService = bookService;
    }

    @PutMapping(path = "/books/{isbn}")
    public ResponseEntity<Book> putBook(@PathVariable final String isbn, @RequestBody final Book book) {
        book.setIsbn(isbn);
        return ResponseEntity.status(HttpStatus.CREATED).body(bookService.create(book));
    }
}

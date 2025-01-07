package com.library.book.controllers;

import com.library.book.domain.Book;
import com.library.book.services.BookService;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @GetMapping(path = "/books/{isbn}")
    public ResponseEntity<Book> retrieveBook(@PathVariable final String isbn) {

        final Optional<Book> foundBook = bookService.findBookByID(isbn);

        return foundBook.map(book -> new ResponseEntity<Book>(book, HttpStatus.OK))
            .orElse(new ResponseEntity<Book>(HttpStatus.NOT_FOUND));

    }

    @GetMapping(path = "/books")
    public ResponseEntity<List<Book>> retrieveAllBooks() {

        final List<Book> allBooks = bookService.findAllBooks();
        return new ResponseEntity<List<Book>>(allBooks, HttpStatus.OK);
    }
    

}

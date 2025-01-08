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

    
    /**
     * PUT /books/{isbn}
     * Updates a book or inserts a new book. The book is identified by its ISBN.
     *
     * @param isbn the ISBN of the book to update
     * @param book the book to update
     * @return the updated/inserted book, with OK status if the book already existed
     *         or CREATED status if the book was inserted
     */
    @PutMapping(path = "/books/{isbn}")
    public ResponseEntity<Book> putUpdateBook(@PathVariable final String isbn, @RequestBody final Book book) {
        
        // Set the isbn of the book to the one provided in the url
        book.setIsbn(isbn);

        // Check if the book already exists in the database
        final boolean isBookExist = bookService.isBookExists(book);

        // Save the book to the database
        final Book savedBook = bookService.save(book);

        // Return the updated/inserted book, with the appropriate http status code
        // If the book already existed, return OK. Otherwise return CREATED.
        return new ResponseEntity<>(savedBook, isBookExist ? HttpStatus.OK : HttpStatus.CREATED);
    }

    /**
     * GET /books/{isbn}
     * Retrieves a book by its ISBN.
     *
     * @param isbn the ISBN of the book to retrieve
     * @return the book if found, or NOT_FOUND status if not found
     */
    @GetMapping(path = "/books/{isbn}")
    public ResponseEntity<Book> retrieveBook(@PathVariable final String isbn) {

        // Attempt to find the book by its ISBN
        final Optional<Book> foundBook = bookService.findBookByID(isbn);

        // Return the found book with OK status, or NOT_FOUND status if no book is found
        return foundBook.map(book -> new ResponseEntity<Book>(book, HttpStatus.OK))
                .orElse(new ResponseEntity<Book>(HttpStatus.NOT_FOUND));
    }

    /**
     * GET /books
     * Retrieves all books in the database.
     *
     * @return a list of all books, with OK status
     */
    @GetMapping(path = "/books")
    public ResponseEntity<List<Book>> retrieveAllBooks() {

        // Retrieve all books in the database
        final List<Book> allBooks = bookService.findAllBooks();

        // Return the list of books with OK status
        return new ResponseEntity<List<Book>>(allBooks, HttpStatus.OK);
    }

}

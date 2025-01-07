package com.library.book.services.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.library.book.domain.Book;
import com.library.book.domain.BookEntity;
import com.library.book.repositories.BookRepository;

@ExtendWith(MockitoExtension.class)
public class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookServiceImpl underTest;

    @Test
    public void testThatBookIsCreated() {

        Book book = TestData.getBook();

        BookEntity bookEntity = TestData.getBookEntity();

        when(bookRepository.save(eq(bookEntity))).thenReturn(bookEntity);

        final Book result = underTest.create(book);

        assertEquals(book, result);
    }

    @Test
    public void testThatFindBookByIDReturnsEmptyWhenNoBookIsFound() {

        final String isbn = "765627459";
        when(bookRepository.findById(eq(isbn))).thenReturn(Optional.empty());

        final Optional<Book> result = underTest.findBookByID(isbn);
        assertEquals(Optional.empty(), result);
    }

    @Test
    public void testThatFindBookByIDReturnsBookWhenExists() {

        final Book book = TestData.getBook();
        final BookEntity bookEntity = TestData.getBookEntity();

        when(bookRepository.findById(eq(book.getIsbn()))).thenReturn(Optional.of(bookEntity));

        Optional<Book> resultOptional = underTest.findBookByID(book.getIsbn());
        assertEquals(Optional.of(book), resultOptional);

    }

    @Test
    public void testThatFindAllBooksReturnsEmptyWhenNoBookIsFound() {

        when(bookRepository.findAll()).thenReturn(new ArrayList<BookEntity>());

        List<Book> allBooks = underTest.findAllBooks();

        assertEquals(0, allBooks.size());
    }

    @Test
    public void testThatFindAllBooksReturnsBookWhenExists() {

        List<BookEntity> listOfBookEntity = TestData.getListOfBookEntity(5);
        when(bookRepository.findAll()).thenReturn(listOfBookEntity);

        List<Book> allBooks = underTest.findAllBooks();

        List<Book> expected = listOfBookEntity.stream().map(this::toBook).toList();

        assertEquals(5, allBooks.size());
        assertEquals(expected, allBooks);
    }
        


    private Book toBook(final BookEntity bookEntity) {
        return Book.builder()
                .isbn(bookEntity.getIsbn())
                .author(bookEntity.getAuthor())
                .title(bookEntity.getTitle())
                .build();
    }

    private BookEntity toBookEntity(final Book book) {
        return BookEntity.builder()
                .isbn(book.getIsbn())
                .author(book.getAuthor())
                .title(book.getTitle())
                .build();
    }
}
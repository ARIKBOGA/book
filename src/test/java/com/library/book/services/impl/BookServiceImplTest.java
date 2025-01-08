package com.library.book.services.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
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

        BookEntity bookEntity = toBookEntity(book);

        when(bookRepository.save(any(BookEntity.class))).thenReturn(bookEntity);

        final Book result = underTest.save(book);

        assertEquals(book, result);
    }

    @Test
    public void testThatFindBookByIDReturnsEmptyWhenNoBookIsFound() {

        final String isbn = "765627459";
        when(bookRepository.findById(eq(isbn))).thenReturn(Optional.empty());

        final Optional<Book> result = underTest.findBookByID(isbn);
        assertEquals(Optional.empty(), result);
    }

    /**
     * Test that the findBookByID method returns a Book when it exists.
     */
    @Test
    public void testThatFindBookByIDReturnsBookWhenExists() {

        final Book book = TestData.getBook();
        final BookEntity bookEntity = toBookEntity(book);

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

    @Test
    public void testThatBookReturnsFalseWhenItDoesNotExists() {

        when(bookRepository.existsById(any())).thenReturn(false);
        final boolean result = underTest.isBookExists(TestData.getBook());

        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testThatBookReturnsTrueWhenItDoesExists() {

        when(bookRepository.existsById(any())).thenReturn(true);
        final boolean result = underTest.isBookExists(TestData.getBook());

        assertEquals(Boolean.TRUE, result);
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
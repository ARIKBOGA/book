package com.library.book.controller;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import com.library.book.domain.Book;
import com.library.book.services.BookService;
import com.library.book.services.impl.TestData;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class BookControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookService bookService;

    /**
     * Tests that a book is created successfully using the put method.
     */
    @Test
    public void testThatBookIsCreatedReturns201() throws Exception {

        // Arrange
        final Book book = TestData.getBook(); // book data returned from the TestData class
        final String bookString = new ObjectMapper().writeValueAsString(book); // converts the book object to a JSON
                                                                               // string

        // Act
        // The put method is used to create a book
        // The content type is set to JSON
        // The book object is converted to a JSON string
        // The book is created
        mockMvc.perform(MockMvcRequestBuilders.put("/books/" + book.getIsbn()).contentType(MediaType.APPLICATION_JSON)
                .content(bookString))

                // Assert
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isbn").value(book.getIsbn()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(book.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.author").value(book.getAuthor()));
    }

    /**
     * Tests that a book is updated successfully using the put method.
     */
    @Test
    public void testThatBookIsUpdatedReturns200() throws Exception {
        // Arrange
        final Book book = TestData.getBook(); // book data returned from the TestData class

        // Save the book to the database, so that it can be updated
        bookService.save(book);

        // Modify the book object
        book.setAuthor(new Faker().book().author());

        // Convert the book object to a JSON string
        final String bookString = new ObjectMapper().writeValueAsString(book);

        // Act
        // The put method is used to update a book
        // The content type is set to JSON
        // The book object is converted to a JSON string
        // The book is updated
        mockMvc.perform(MockMvcRequestBuilders.put("/books/" + book.getIsbn()).contentType(MediaType.APPLICATION_JSON)
                .content(bookString))

                // Assert
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isbn").value(book.getIsbn()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(book.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.author").value(book.getAuthor()));
    }

    @Test
    public void testThatBookIsNotFoundAndReturns404() throws Exception {

        final String isbn = "123456789"; // isbn of a book that does not exist

        mockMvc.perform(MockMvcRequestBuilders.get("/books/" + isbn))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testThatBookIsFoundAndReturns200() throws Exception {

        final Book book = TestData.getBook();
        final String isbn = book.getIsbn();

        bookService.save(book);

        mockMvc.perform(MockMvcRequestBuilders.get("/books/" + isbn)).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isbn").value(book.getIsbn()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(book.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.author").value(book.getAuthor()));
    }

    @Test
    public void testThatRetrieveAllBooksReturns200EmptyListWhenNoBookIsFound() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/books")).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("[]"))
                .andExpect(MockMvcResultMatchers.jsonPath("$").isEmpty());

    }

    @Test
    public void testThatRetrieveAllBooksReturns200ListOfBooksWhenBooksExist() throws Exception {

        List<Book> listOfBook = TestData.getListOfBook(10);

        for (Book book : listOfBook) {
            bookService.save(book);
        }

        mockMvc.perform(MockMvcRequestBuilders.get("/books"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(new ObjectMapper().writeValueAsString(listOfBook)));
        
        for (int i = 0; i < listOfBook.size(); i++) {
            mockMvc.perform(MockMvcRequestBuilders.get("/books"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[" + i + "].isbn").value(listOfBook.get(i).getIsbn()))
                .andExpect(MockMvcResultMatchers.jsonPath("$..[" + i + "].title").value(listOfBook.get(i).getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[" + i + "]author").value(listOfBook.get(i).getAuthor()));;
        }


    }
}
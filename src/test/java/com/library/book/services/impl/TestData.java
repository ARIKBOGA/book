package com.library.book.services.impl;

import com.library.book.domain.Book;
import com.library.book.domain.BookEntity;

public final class TestData {

    private TestData() {}

    public static Book getBook() {
        return Book.builder()
                .isbn("067576234")
                .author("Wirginia Wolf")
                .title("The Waves")
                .build();
    }

    public static BookEntity getBookEntity() {
        return BookEntity.builder()
                .isbn("067576234")
                .author("Wirginia Wolf")
                .title("The Waves")
                .build();
    }
    
}

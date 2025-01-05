package com.library.book.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.library.book.domain.BookEntity;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, String> {

}
package com.example.demo.model.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.demo.model.Book;

public interface BookRepository extends MongoRepository<Book, String>
{
    List findByTitleContaining(String title);
    List findByAuthor(String name);
}

package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Book;
import com.example.demo.model.repositories.BookRepository;

@CrossOrigin("http://localhost:8080")
@RestController
@RequestMapping("/v1/mongodbapp")
public class BookController
{
    @Autowired
    BookRepository bookRepository;

    @GetMapping("/books")
    public ResponseEntity<List> getAllBooks(@RequestParam(required = false) String bookTitle)
    {
        try
        {
            List listOfBooks = new ArrayList<>();
            if(bookTitle == null || bookTitle.isEmpty())
            {
                bookRepository.findAll().forEach(listOfBooks::add);
            }
            else
            {
                bookRepository.findByTitleContaining(bookTitle).forEach(listOfBooks::add);
            }

            if(listOfBooks.isEmpty())
            {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(listOfBooks, HttpStatus.OK);
        }
        catch (Exception e)
        {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/books/{id}")
    public ResponseEntity getBookById(@PathVariable("id") String id)
    {
        try
        {
            Optional bookOptional = bookRepository.findById(id);

            return new ResponseEntity<>(bookOptional.get(), HttpStatus.OK);
        }
        catch (Exception e)
        {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/books")
    public ResponseEntity addABookToLibrary(@RequestBody Book book)
    {
    	System.out.println("Called Post mehod*******");
    	System.out.println("Called Post 2__mehod*******");
        try
        {
            Book createdBook = bookRepository.save(new Book(book.getTitle(), book.getAuthor(),
                    book.getIsbn()));
            return new ResponseEntity<>(createdBook, HttpStatus.CREATED);
        }
        catch (Exception e)
        {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/books/{id}")
    public ResponseEntity updateABook(@PathVariable("id") String id, @RequestBody Book book)
    {
        Optional bookOptional = bookRepository.findById(id);

        if(bookOptional.isPresent())
        {
            Book updatedBook = (Book) bookOptional.get();
            updatedBook.setTitle(book.getTitle());
            updatedBook.setAuthor(book.getAuthor());
            updatedBook.setIsbn(book.getIsbn());
            return new ResponseEntity<>(bookRepository.save(updatedBook), HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/books/{id}")
    public ResponseEntity deleteABook(@PathVariable("id") String id)
    {
        try
        {
            bookRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        catch (Exception e)
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}


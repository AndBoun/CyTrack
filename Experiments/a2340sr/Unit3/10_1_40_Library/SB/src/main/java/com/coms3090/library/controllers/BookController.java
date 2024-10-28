package com.coms3090.library.controllers;

import com.coms3090.library.entities.Book;
import com.coms3090.library.repositories.BookRepository;
import com.coms3090.library.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookService bookService; // Injecting BookService
    // private BookRepository bookRepository;

    @GetMapping
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    // Endpoint to get a book by its ID
    @GetMapping("/books/{id}")
    public Optional<Book> getBookById(@PathVariable Long id) {
        return bookService.getBookById(id);
    }

    @PostMapping
    public Book createBook(@RequestBody Book book) {
        return bookService.createBook(book);
    }

    // Endpoint to delete a book by its ID
    @DeleteMapping("/books/{id}")
    public String deleteBookById(@PathVariable Long id) {
        bookService.deleteBook(id);
        return "Book with ID " + id + " has been deleted.";
    }

    // Additional endpoints can be added as needed
}

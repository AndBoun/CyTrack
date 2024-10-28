package com.coms3090.library.controllers;

import com.coms3090.library.entities.Author;
import com.coms3090.library.entities.Book;
import com.coms3090.library.repositories.AuthorRepository;
import com.coms3090.library.services.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/authors")
public class AuthorController {

    @Autowired
    private AuthorService authorService;
    // private AuthorRepository authorRepository;

    @GetMapping
    public List<Author> getAllAuthors() {
        return authorService.getAllAuthors();
    }

    // Endpoint to get a book by its ID
    @GetMapping("/authors/{id}")
    public Optional<Author> getAuthorById(@PathVariable Long id) {
        return authorService.getAuthorById(id);
    }

    @PostMapping
    public Author createAuthor(@RequestBody Author author) {
        return authorService.createAuthor(author);
    }

    // Endpoint to delete a book by its ID
    @DeleteMapping("/authors/{id}")
    public String deleteAuthorById(@PathVariable Long id) {
        authorService.deleteAuthor(id);
        return "Book with ID " + id + " has been deleted.";
    }
    // Additional endpoints can be added as needed
}

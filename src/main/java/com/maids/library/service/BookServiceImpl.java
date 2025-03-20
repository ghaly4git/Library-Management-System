package com.maids.library.service;

import com.maids.library.Book;
import com.maids.library.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public Book getBookById(Long id) {
        Optional<Book> book = bookRepository.findById(id);
        return book.orElse(null);
    }

    @Override
    public Book addBook(Book book) {
        book.setId(null); // Ensure ID is null for new books
        return bookRepository.save(book);
    }

    @Override
    public Book updateBook(Long id, Book book) {
        if (!bookRepository.existsById(id)) {
            throw new IllegalArgumentException("Book with ID " + id + " does not exist");
        }
        book.setId(id);
        return bookRepository.save(book);
    }

    @Override
    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new IllegalArgumentException("Book with ID " + id + " does not exist");
        }
        bookRepository.deleteById(id);
    }
}
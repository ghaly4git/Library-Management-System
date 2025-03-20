package com.maids.library.controller;

import com.maids.library.Book;
import com.maids.library.BorrowingRecord;
import com.maids.library.Patron;
import com.maids.library.service.BookService;
import com.maids.library.service.BorrowingRecordService;
import com.maids.library.service.PatronService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class LibraryController {

    @Autowired
    private BookService bookService;

    @Autowired
    private PatronService patronService;

    @Autowired
    private BorrowingRecordService borrowingRecordService;

    // Book Endpoints
    @GetMapping("/books")
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/books/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        Book book = bookService.getBookById(id);
        return book != null ? ResponseEntity.ok(book) : ResponseEntity.notFound().build();
    }

    @PostMapping("/books")
    public ResponseEntity<Book> addBook(@Valid @RequestBody Book book) {
        Book savedBook = bookService.addBook(book);
        return ResponseEntity.ok(savedBook);
    }

    @PutMapping("/books/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @Valid @RequestBody Book book) {
        Book updatedBook = bookService.updateBook(id, book);
        return updatedBook != null ? ResponseEntity.ok(updatedBook) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/books/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    // Patron Endpoints
    @GetMapping("/patrons")
    public List<Patron> getAllPatrons() {
        return patronService.getAllPatrons();
    }

    @GetMapping("/patrons/{id}")
    public ResponseEntity<Patron> getPatronById(@PathVariable Long id) {
        Patron patron = patronService.getPatronById(id);
        return patron != null ? ResponseEntity.ok(patron) : ResponseEntity.notFound().build();
    }

    @PostMapping("/patrons")
    public ResponseEntity<Patron> addPatron(@Valid @RequestBody Patron patron) {
        Patron savedPatron = patronService.addPatron(patron);
        return ResponseEntity.ok(savedPatron);
    }

    @PutMapping("/patrons/{id}")
    public ResponseEntity<Patron> updatePatron(@PathVariable Long id, @Valid @RequestBody Patron patron) {
        Patron updatedPatron = patronService.updatePatron(id, patron);
        return updatedPatron != null ? ResponseEntity.ok(updatedPatron) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/patrons/{id}")
    public ResponseEntity<Void> deletePatron(@PathVariable Long id) {
        patronService.deletePatron(id);
        return ResponseEntity.noContent().build();
    }

    // Borrowing Record Endpoints
    @PostMapping("/borrow/{bookId}/{patronId}")
    public ResponseEntity<BorrowingRecord> borrowBook(@PathVariable Long bookId, @PathVariable Long patronId) {
        BorrowingRecord record = borrowingRecordService.borrowBook(bookId, patronId);
        return ResponseEntity.ok(record);
    }

    @PutMapping("/return/{bookId}/{patronId}")
    public ResponseEntity<Void> returnBook(@PathVariable Long bookId, @PathVariable Long patronId) {
        borrowingRecordService.returnBook(bookId, patronId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/borrowing-records")
    public List<BorrowingRecord> getAllBorrowingRecords() {
        return borrowingRecordService.getAllBorrowingRecords();
    }
}
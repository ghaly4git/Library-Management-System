package com.maids.library.service;

import com.maids.library.Book;
import com.maids.library.BorrowingRecord;
import com.maids.library.Patron;
import com.maids.library.repository.BookRepository;
import com.maids.library.repository.BorrowingRecordRepository;
import com.maids.library.repository.PatronRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class BorrowingRecordServiceImpl implements BorrowingRecordService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private PatronRepository patronRepository;

    @Autowired
    private BorrowingRecordRepository borrowingRecordRepository;

    @Override
    public BorrowingRecord borrowBook(Long bookId, Long patronId) {
        // Check if book and patron exist
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book with ID " + bookId + " does not exist"));
        Patron patron = patronRepository.findById(patronId)
                .orElseThrow(() -> new IllegalArgumentException("Patron with ID " + patronId + " does not exist"));

        // Check if the book is available
        if (!book.isAvailable()) {
            throw new IllegalArgumentException("Book with ID " + bookId + " is not available");
        }

        // Check if there's an active borrowing record for this book
        Optional<BorrowingRecord> existingRecord = borrowingRecordRepository
                .findByBookIdAndReturnDateIsNull(bookId);
        if (existingRecord.isPresent()) {
            throw new IllegalArgumentException("Book with ID " + bookId + " is already borrowed");
        }

        // Create a new borrowing record
        BorrowingRecord record = new BorrowingRecord(book, patron, LocalDate.now(), null);

        // Update book availability
        book.setAvailable(false);
        bookRepository.save(book);

        return borrowingRecordRepository.save(record);
    }

    @Override
    public void returnBook(Long bookId, Long patronId) {
        // Find the active borrowing record
        BorrowingRecord record = borrowingRecordRepository
                .findByBookIdAndPatronIdAndReturnDateIsNull(bookId, patronId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No active borrowing record found for book ID " + bookId + " and patron ID " + patronId));

        // Update the return date
        record.setReturnDate(LocalDate.now());
        borrowingRecordRepository.save(record);

        // Update book availability
        Book book = record.getBook();
        book.setAvailable(true);
        bookRepository.save(book);
    }

    @Override
    public List<BorrowingRecord> getAllBorrowingRecords() {
        return borrowingRecordRepository.findAll();
    }
}
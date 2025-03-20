package com.maids.library.service;
import com.maids.library.Book; // Import Book
import com.maids.library.repository.BookRepository;
import com.maids.library.BorrowingRecord;
import com.maids.library.Patron;
import com.maids.library.repository.BorrowingRecordRepository;
import com.maids.library.repository.PatronRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = com.maids.library.LibraryManagementSystemApplication.class)
public class BorrowingRecordServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private PatronRepository patronRepository;

    @Mock
    private BorrowingRecordRepository borrowingRecordRepository;

    @InjectMocks
    private BorrowingRecordServiceImpl borrowingRecordService;

    private Book book;
    private Patron patron;
    private BorrowingRecord borrowingRecord;

    @BeforeEach
    void setUp() {
        book = new Book();
        book.setId(1L);
        book.setTitle("The Great Gatsby");
        book.setAuthor("F. Scott Fitzgerald");
        book.setPublicationYear(1925);
        book.setIsbn("978-0743273565");
        book.setAvailable(true);

        patron = new Patron();
        patron.setId(1L);
        patron.setName("John Doe");
        patron.setEmail("john.doe@example.com");

        borrowingRecord = new BorrowingRecord(book, patron, LocalDate.now(), null);
        borrowingRecord.setId(1L);
    }

    @Test
    void testBorrowBook() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(patronRepository.findById(1L)).thenReturn(Optional.of(patron));
        when(borrowingRecordRepository.findByBookIdAndReturnDateIsNull(1L)).thenReturn(Optional.empty());
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        when(borrowingRecordRepository.save(any(BorrowingRecord.class))).thenReturn(borrowingRecord);

        BorrowingRecord result = borrowingRecordService.borrowBook(1L, 1L);
        assertNotNull(result);
        assertEquals(book, result.getBook());
        assertEquals(patron, result.getPatron());
        assertFalse(book.isAvailable());
    }

    @Test
    void testReturnBook() {
        when(borrowingRecordRepository.findByBookIdAndPatronIdAndReturnDateIsNull(1L, 1L)).thenReturn(Optional.of(borrowingRecord));
        when(borrowingRecordRepository.save(any(BorrowingRecord.class))).thenReturn(borrowingRecord);
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        borrowingRecordService.returnBook(1L, 1L);
        assertNotNull(borrowingRecord.getReturnDate());
        assertTrue(book.isAvailable());
    }

    @Test
    void testGetAllBorrowingRecords() {
        List<BorrowingRecord> records = Arrays.asList(borrowingRecord);
        when(borrowingRecordRepository.findAll()).thenReturn(records);

        List<BorrowingRecord> result = borrowingRecordService.getAllBorrowingRecords();
        assertEquals(1, result.size());
        assertEquals(borrowingRecord, result.get(0));
    }
}
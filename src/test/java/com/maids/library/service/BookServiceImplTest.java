package com.maids.library.service;

import com.maids.library.Book;
import com.maids.library.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = com.maids.library.LibraryManagementSystemApplication.class)
public class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    private Book book;

    @BeforeEach
    void setUp() {
        book = new Book();
        book.setId(1L);
        book.setTitle("The Great Gatsby");
        book.setAuthor("F. Scott Fitzgerald");
        book.setPublicationYear(1925);
        book.setIsbn("978-0743273565");
        book.setAvailable(true);
    }

    @Test
    void testGetAllBooks() {
        List<Book> books = Arrays.asList(book);
        when(bookRepository.findAll()).thenReturn(books);

        List<Book> result = bookService.getAllBooks();
        assertEquals(1, result.size());
        assertEquals("The Great Gatsby", result.get(0).getTitle());
    }

    @Test
    void testGetBookById() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        Book result = bookService.getBookById(1L);
        assertNotNull(result);
        assertEquals("The Great Gatsby", result.getTitle());
    }

    @Test
    void testAddBook() {
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        Book result = bookService.addBook(book);
        assertNotNull(result);
        assertEquals("The Great Gatsby", result.getTitle());
    }

    @Test
    void testUpdateBook() {
        when(bookRepository.existsById(1L)).thenReturn(true);
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        Book updatedBook = bookService.updateBook(1L, book);
        assertNotNull(updatedBook);
        assertEquals("The Great Gatsby", updatedBook.getTitle());
    }

    @Test
    void testDeleteBook() {
        when(bookRepository.existsById(1L)).thenReturn(true);
        doNothing().when(bookRepository).deleteById(1L);

        bookService.deleteBook(1L);
        verify(bookRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteBookNotFound() {
        when(bookRepository.existsById(1L)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> bookService.deleteBook(1L));
    }
}
package com.maids.library.service;
import com.maids.library.Patron;
import com.maids.library.repository.PatronRepository;
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
public class PatronServiceImplTest {

    @Mock
    private PatronRepository patronRepository;

    @InjectMocks
    private PatronServiceImpl patronService;

    private Patron patron;

    @BeforeEach
    void setUp() {
        patron = new Patron();
        patron.setId(1L);
        patron.setName("John Doe");
        patron.setEmail("john.doe@example.com");
    }

    @Test
    void testGetAllPatrons() {
        List<Patron> patrons = Arrays.asList(patron);
        when(patronRepository.findAll()).thenReturn(patrons);

        List<Patron> result = patronService.getAllPatrons();
        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getName());
    }

    @Test
    void testGetPatronById() {
        when(patronRepository.findById(1L)).thenReturn(Optional.of(patron));

        Patron result = patronService.getPatronById(1L);
        assertNotNull(result);
        assertEquals("John Doe", result.getName());
    }

    @Test
    void testAddPatron() {
        when(patronRepository.save(any(Patron.class))).thenReturn(patron);

        Patron result = patronService.addPatron(patron);
        assertNotNull(result);
        assertEquals("John Doe", result.getName());
    }

    @Test
    void testUpdatePatron() {
        when(patronRepository.existsById(1L)).thenReturn(true);
        when(patronRepository.save(any(Patron.class))).thenReturn(patron);

        Patron updatedPatron = patronService.updatePatron(1L, patron);
        assertNotNull(updatedPatron);
        assertEquals("John Doe", updatedPatron.getName());
    }

    @Test
    void testDeletePatron() {
        when(patronRepository.existsById(1L)).thenReturn(true);
        doNothing().when(patronRepository).deleteById(1L);

        patronService.deletePatron(1L);
        verify(patronRepository, times(1)).deleteById(1L);
    }
}
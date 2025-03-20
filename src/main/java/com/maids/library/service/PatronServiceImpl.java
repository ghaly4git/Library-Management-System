package com.maids.library.service;

import com.maids.library.Patron;
import com.maids.library.repository.PatronRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatronServiceImpl implements PatronService {

    @Autowired
    private PatronRepository patronRepository;

    @Override
    public List<Patron> getAllPatrons() {
        return patronRepository.findAll();
    }

    @Override
    public Patron getPatronById(Long id) {
        Optional<Patron> patron = patronRepository.findById(id);
        return patron.orElse(null);
    }

    @Override
    public Patron addPatron(Patron patron) {
        patron.setId(null); // Ensure ID is null for new patrons
        return patronRepository.save(patron);
    }

    @Override
    public Patron updatePatron(Long id, Patron patron) {
        if (!patronRepository.existsById(id)) {
            throw new IllegalArgumentException("Patron with ID " + id + " does not exist");
        }
        patron.setId(id);
        return patronRepository.save(patron);
    }

    @Override
    public void deletePatron(Long id) {
        if (!patronRepository.existsById(id)) {
            throw new IllegalArgumentException("Patron with ID " + id + " does not exist");
        }
        patronRepository.deleteById(id);
    }
}
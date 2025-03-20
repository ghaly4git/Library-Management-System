package com.maids.library.service;

import com.maids.library.BorrowingRecord;

import java.util.List;

public interface BorrowingRecordService {
    BorrowingRecord borrowBook(Long bookId, Long patronId);
    void returnBook(Long bookId, Long patronId);
    List<BorrowingRecord> getAllBorrowingRecords();
}
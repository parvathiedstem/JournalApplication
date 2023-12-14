package com.Eudaimonia.DigitalJournalApp.repository;

import com.Eudaimonia.DigitalJournalApp.model.Journal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JournalRepository extends JpaRepository<Journal, Long> {
    List<Journal> findByDeleted(boolean deleted);

    Page<Journal> findByCategoryAndDeletedFalse(String search, Pageable pageable);

    Page<Journal> findByTitleAndDeletedFalse(String search, Pageable pageable);

    Page<Journal> findAllByDeletedFalse(Pageable pageable);
}

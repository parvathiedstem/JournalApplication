package com.Eudaimonia.DigitalJournalApp.repository;

import com.Eudaimonia.DigitalJournalApp.model.Journal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JournalRepository extends JpaRepository<Journal, Long> {
}

package com.Eudaimonia.DigitalJournalApp.repository;

import com.Eudaimonia.DigitalJournalApp.model.Journal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JournalRepository extends JpaRepository<Journal, Long> {
}

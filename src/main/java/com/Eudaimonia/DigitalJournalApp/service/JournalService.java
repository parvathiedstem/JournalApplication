package com.Eudaimonia.DigitalJournalApp.service;

import com.Eudaimonia.DigitalJournalApp.contract.JournalRequest;
import com.Eudaimonia.DigitalJournalApp.contract.JournalResponse;
import com.Eudaimonia.DigitalJournalApp.model.Journal;
import com.Eudaimonia.DigitalJournalApp.repository.JournalRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JournalService {
    private final JournalRepository journalRepository;

    private final ModelMapper modelMapper;
    public Long CreateJournal(JournalRequest request) {
        Locale locale = new Locale("fr", "FR");
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT, locale);
        String date = dateFormat.format(new Date());

        System.out.print(date);
        Journal journal = Journal.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .createdAt(LocalDate.now())
                .build();
        journal = journalRepository.save(journal);
        return journal.getId();
    }

    public JournalResponse GetJournalById(Long id) {
        Journal journal = journalRepository.findById(id).
                orElseThrow(() -> new RuntimeException("journal not found"));
        JournalResponse response = modelMapper.map(journal, JournalResponse.class);
        return response;
    }

    public JournalResponse UpdateJournalById(Long id, JournalRequest request) {
        Journal journal = journalRepository.findById(id).
                orElseThrow(() -> new RuntimeException("journal not found"));

        journal.setTitle(request.getTitle());
        journal.setContent(request.getContent());
        journal.setUpdatedAt(LocalDateTime.now());
        journalRepository.save(journal);
        return modelMapper.map(journal, JournalResponse.class);
    }

    public void RemoveJournalById(Long id) {
        journalRepository.findById(id).
                orElseThrow(() -> new RuntimeException("journal not found"));
        journalRepository.deleteById(id);
    }

    public List<JournalResponse> GetJournalList() {
        List<Journal> journals = journalRepository.findAll();
        return journals.stream().map(journal -> modelMapper.map(journal,JournalResponse.class))
                .collect(Collectors.toList());
    }
}

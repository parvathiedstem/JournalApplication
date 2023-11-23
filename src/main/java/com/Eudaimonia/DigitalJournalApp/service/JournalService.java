package com.Eudaimonia.DigitalJournalApp.service;

import com.Eudaimonia.DigitalJournalApp.contract.Request.JournalRequest;
import com.Eudaimonia.DigitalJournalApp.contract.Response.JournalResponse;
import com.Eudaimonia.DigitalJournalApp.model.Category;
import com.Eudaimonia.DigitalJournalApp.model.Journal;
import com.Eudaimonia.DigitalJournalApp.repository.CategoryRepository;
import com.Eudaimonia.DigitalJournalApp.repository.JournalRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JournalService {
    private final JournalRepository journalRepository;
    private final CategoryRepository categoryRepository;

    private final ModelMapper modelMapper;

    @Transactional
    public Long createJournal(JournalRequest request)throws IOException {
        Category category = categoryRepository.findByName(request.getCategory());
        if(category != null){
            Journal journal = Journal.builder()
                    .title(request.getTitle())
                    .content(request.getContent())
                    .createdAt(LocalDate.now())
                    .category(request.getCategory())
                    .updatedAt(LocalDateTime.now())
                    .build();
            journal = journalRepository.save(journal);
            return journal.getId();
        }else {
            throw new RuntimeException("category not found please add new category");
        }
    }

    public JournalResponse GetJournalById(Long id) throws IOException {
        Journal journal = journalRepository.findById(id).
                orElseThrow(() -> new RuntimeException("journal not found"));
        return modelMapper.map(journal, JournalResponse.class);
    }

    public JournalResponse UpdateJournalById(Long id, JournalRequest request) throws IOException{
        Journal journal = journalRepository.findById(id).
                orElseThrow(() -> new RuntimeException("journal not found"));

        journal.setTitle(request.getTitle());
        journal.setContent(request.getContent());
        journal.setUpdatedAt(LocalDateTime.now());
        journalRepository.save(journal);
        return modelMapper.map(journal, JournalResponse.class);
    }

    public void SoftDeleteJournalById(Long id) throws IOException{
        Journal journal = journalRepository.findById(id).
                orElseThrow(() -> new RuntimeException("journal not found"));
        if(!journal.isDeleted())
        {
            journal.setDeleted(true);
            journal.setUpdatedAt(LocalDateTime.now());
            journalRepository.save(journal);
        }else {
            throw new RuntimeException("journal already deleted");
        }

    }

    public void RemoveJournalById(Long id) throws IOException{
        Journal journal = journalRepository.findById(id).
                orElseThrow(() -> new RuntimeException("journal not found"));
        if(journal.isDeleted())
        {
            journalRepository.deleteById(id);
            journalRepository.save(journal);
        }else {
            throw new RuntimeException("journal already deleted");
        }
    }

    public void restoreJournalById(Long id) throws IOException {
        Journal journal = journalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Journal not found"));

        if (journal.isDeleted()) {
            journal.setDeleted(false);
            journal.setUpdatedAt(LocalDateTime.now());
            journalRepository.save(journal);
        } else {
            throw new RuntimeException("Journal is not deleted");
        }
    }

    @Transactional
    public List<JournalResponse> searchList(int page, int size, String search) throws IOException {
        List<Journal> journals = journalRepository.findAll();
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "updatedAt"));

        Page<Journal> journalPage;

        if (journals.stream().anyMatch(journal -> Objects.equals(search, journal.getCategory())) && !search.isEmpty()) {
            journalPage = journalRepository.findByCategory(search, pageable);
        } else if (journals.stream().anyMatch(journal -> Objects.equals(search, journal.getTitle())) && !search.isEmpty()) {
            journalPage = journalRepository.findByTitle(search, pageable);
        } else {
            journalPage = journalRepository.findAll(pageable);
        }

        return journalPage.getContent().stream()
                .map(journal -> modelMapper.map(journal, JournalResponse.class))
                .sorted(Comparator.comparing(JournalResponse::getUpdatedAt, Comparator.nullsLast(Comparator.reverseOrder())))
                .collect(Collectors.toList());
    }

    public List<JournalResponse> getJournalList() throws IOException {
        List<Journal> journals = journalRepository.findAll();
            return journals.stream().map(journal -> modelMapper.map(journal,JournalResponse.class))
                    .sorted(Comparator.comparing(JournalResponse::getUpdatedAt,Comparator.nullsLast(Comparator.reverseOrder())))
                    .collect(Collectors.toList());
    }

    @Transactional
    public List<JournalResponse> getJournalTrashList() throws IOException {
        List<Journal> journals = journalRepository.findByDeleted(true);
        return journals.stream().map(journal -> modelMapper.map(journal,JournalResponse.class))
                .sorted(Comparator.comparing(JournalResponse::getUpdatedAt,Comparator.nullsLast(Comparator.reverseOrder())))
                .collect(Collectors.toList());
    }
}

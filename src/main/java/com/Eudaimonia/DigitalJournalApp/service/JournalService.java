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
import java.text.DateFormat;
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
                    .build();
            journal = journalRepository.save(journal);
            return journal.getId();
        }else {
            throw new RuntimeException("category not found please add new category");
        }

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

    @Transactional
    public List<JournalResponse> getJournalList(int page, int size, String search) throws IOException {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "updatedAt"));

        Page<Journal> journalPage;
        if (search != null && !search.isEmpty()) {
            journalPage = journalRepository.findByCategory(search, pageable);
        } else {
            journalPage = journalRepository.findAll(pageable);
        }

        return journalPage.getContent().stream()
                .map(journal -> modelMapper.map(journal, JournalResponse.class))
                .sorted(Comparator.comparing(JournalResponse::getUpdatedAt,Comparator.nullsLast(Comparator.naturalOrder())))
                .collect(Collectors.toList());
    }
}

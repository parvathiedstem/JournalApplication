package com.Eudaimonia.DigitalJournalApp.service;

import com.Eudaimonia.DigitalJournalApp.configuration.UserInfoUserDetails;
import com.Eudaimonia.DigitalJournalApp.contract.request.JournalRequest;
import com.Eudaimonia.DigitalJournalApp.contract.response.JournalResponse;
import com.Eudaimonia.DigitalJournalApp.contract.response.PaginationResponse;
import com.Eudaimonia.DigitalJournalApp.model.Category;
import com.Eudaimonia.DigitalJournalApp.model.Journal;
import com.Eudaimonia.DigitalJournalApp.model.User;
import com.Eudaimonia.DigitalJournalApp.repository.CategoryRepository;
import com.Eudaimonia.DigitalJournalApp.repository.JournalRepository;
import com.Eudaimonia.DigitalJournalApp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    @Transactional
    public Long createJournal(JournalRequest request) {
        Category category = categoryRepository.findByName(request.getCategory());
        UserInfoUserDetails userDetails = (UserInfoUserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        Optional<User> user = userRepository.findById(userDetails.getId());
        if(category != null){
            Journal journal = Journal.builder()
                    .title(request.getTitle())
                    .content(request.getContent())
                    .createdAt(LocalDate.now())
                    .category(request.getCategory())
                    .updatedAt(LocalDateTime.now())
                    .user(user.get())
                    .build();
            journal = journalRepository.save(journal);
            return journal.getId();
        }else {
            throw new RuntimeException("category not found please add new category");
        }
    }

    public JournalResponse GetJournalById(Long id){
        Journal journal = journalRepository.findById(id).
                orElseThrow(() -> new RuntimeException("journal not found"));
        return modelMapper.map(journal, JournalResponse.class);
    }

    public JournalResponse UpdateJournalById(Long id, JournalRequest request) {
        Journal journal = journalRepository.findById(id).
                orElseThrow(() -> new RuntimeException("journal not found"));
        UserInfoUserDetails userDetails = (UserInfoUserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        if(Objects.equals(userDetails.getId(), journal.getUser().getId())) {
            journal.setTitle(request.getTitle());
            journal.setContent(request.getContent());
            journal.setUpdatedAt(LocalDateTime.now());
            journalRepository.save(journal);
            return modelMapper.map(journal, JournalResponse.class);
        }
        else {
            throw new RuntimeException("only creator can update the journal");
        }
    }

    public void SoftDeleteJournalById(Long id){
        Journal journal = journalRepository.findById(id).
                orElseThrow(() -> new RuntimeException("journal not found"));
        UserInfoUserDetails userDetails = (UserInfoUserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        if(Objects.equals(userDetails.getId(), journal.getUser().getId())) {
            if(!journal.isDeleted())
            {
                journal.setDeleted(true);
                journal.setUpdatedAt(LocalDateTime.now());
                journalRepository.save(journal);
            }else {
                throw new RuntimeException("journal already deleted");
            }
        }
        else {
            throw new RuntimeException("only creator can update the journal");
        }
    }

    public void RemoveJournalById(Long id) {
        Journal journal = journalRepository.findById(id).
                orElseThrow(() -> new RuntimeException("journal not found"));
        UserInfoUserDetails userDetails = (UserInfoUserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        if(Objects.equals(userDetails.getId(), journal.getUser().getId())) {

            if (journal.isDeleted()) {
                journalRepository.deleteById(id);
            } else {
                throw new RuntimeException("journal cant delete");
            }
        }
        else {
            throw new RuntimeException("only creator can remove the journal");
        }
    }

    public void restoreJournalById(Long id) {
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
    public List<JournalResponse> searchList(String search) {
        List<Journal> journals = journalRepository.findAll();

        if (journals.stream().anyMatch(journal -> !journal.isDeleted())) {
            List<Journal> filteredJournals = journals.stream()
                    .filter(journal -> !journal.isDeleted() &&
                            (journal.getCategory().contains(search) || journal.getTitle().contains(search)))
                    .toList();

            return filteredJournals.stream()
                    .map(journal -> modelMapper.map(journal, JournalResponse.class))
                    .sorted(Comparator.comparing(JournalResponse::getUpdatedAt, Comparator.nullsLast(Comparator.reverseOrder())))
                    .collect(Collectors.toList());

        } else {
            throw new RuntimeException("error while getting data");
        }
    }

    @Transactional
    public PaginationResponse getJournalList(int page, int size) {
        UserInfoUserDetails userDetails = (UserInfoUserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        Page<Journal> journalPage = journalRepository.findByUserId(
                userDetails.getId(),
                PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "updatedAt"))
        );

        if (!journalPage.isEmpty()) {
            List<JournalResponse> journalResponses = journalPage.getContent().stream()
                    .filter(journal -> !journal.isDeleted())
                    .map(journal -> modelMapper.map(journal, JournalResponse.class))
                    .sorted(Comparator.comparing(JournalResponse::getUpdatedAt, Comparator.nullsLast(Comparator.reverseOrder())))
                    .collect(Collectors.toList());

            return new PaginationResponse(
                    journalResponses,
                    journalPage.getNumber(),
                    journalPage.getSize(),
                    journalPage.getTotalElements()
            );
        } else {
            throw new RuntimeException("No journals found for the user");
        }
    }


    @Transactional
    public List<JournalResponse> getJournalTrashList() {
        List<Journal> journals = journalRepository.findByDeleted(true);
        return journals.stream().map(journal -> modelMapper.map(journal,JournalResponse.class))
                .sorted(Comparator.comparing(JournalResponse::getUpdatedAt,Comparator.nullsLast(Comparator.reverseOrder())))
                .collect(Collectors.toList());
    }
}

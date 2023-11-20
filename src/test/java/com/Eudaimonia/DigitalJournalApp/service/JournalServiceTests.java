package com.Eudaimonia.DigitalJournalApp.service;

import com.Eudaimonia.DigitalJournalApp.contract.Request.JournalRequest;
import com.Eudaimonia.DigitalJournalApp.contract.Response.JournalResponse;
import com.Eudaimonia.DigitalJournalApp.model.Journal;
import com.Eudaimonia.DigitalJournalApp.repository.JournalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@AutoConfigureTestDatabase
public class JournalServiceTests {
    private JournalRepository journalRepository;
    private JournalService journalService;
    private ModelMapper modelMapper;

    @BeforeEach
    public void init(){
        MockitoAnnotations.openMocks(this);
        journalRepository = Mockito.mock(JournalRepository.class);
        modelMapper = Mockito.mock(ModelMapper.class);
        journalService = new JournalService(journalRepository, modelMapper);
    }

    @Test
    void testCreateJournal(){
        Journal entity = new Journal();
        entity.setTitle("abc");
        entity.setContent("about wings of fire");
        entity.setCreatedAt(LocalDate.now());
        entity.setId(1L);
        JournalRequest request = new JournalRequest();
        when(journalRepository.save(any())).thenReturn(entity);
        long id = journalService.createJournal(request);

        assertEquals(id,1L);
    }

    @Test
    void testGetTaskById(){
        Journal entity = new Journal();
        entity.setTitle("abc");
        entity.setContent("about wings of fire");
        entity.setId(1L);
        entity.setCreatedAt(LocalDate.now());

        JournalResponse response = new JournalResponse();
        response.setTitle("abc");
        response.setContent("about wings of fire");
        response.setId(1L);;
        response.setCreatedAt(LocalDate.now());

        when(journalRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(modelMapper.map(entity, JournalResponse.class)).thenReturn(response);

        JournalResponse result = journalService.GetJournalById(1L);

        assertEquals(result.getId(),1L);
        assertEquals(result.getTitle(),"abc");
    }

    @Test
    void testUpdateJournal(){
        Journal entity = new Journal();
        entity.setTitle("abc");
        entity.setContent("about wings of fire");
        entity.setCreatedAt(LocalDate.now());
        entity.setUpdatedAt(LocalDateTime.now());
        entity.setId(1L);
        JournalRequest request = new JournalRequest();
        JournalResponse response = new JournalResponse();
        response.setTitle("abc");
        response.setContent("about wings of fire");
        response.setId(1L);;
        response.setCreatedAt(LocalDate.now());

        when(journalRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(journalRepository.save(any())).thenReturn(entity);
        when(modelMapper.map(entity, JournalResponse.class)).thenReturn(response);

        JournalResponse result = journalService.UpdateJournalById(1L, request);

        assertEquals(result.getId(),1L);
    }

    @Test
    void testDeleteJournal(){
        Journal journal = new Journal();
        when(journalRepository.findById(1L)).thenReturn(Optional.of(journal));
        doNothing().when(journalRepository).delete(journal);

        journalService.RemoveJournalById(1L);

        verify(journalRepository, times(1)).findById(1L);
        verify(journalRepository, times(1)).deleteById(1L);
    }

//    @Test
//    public void testListJournals() throws IOException {
//        // Given
//        List<Journal> journals = new ArrayList<>();
//        journals.add(new Journal(1L, "wings","content",LocalDate.now(),LocalDateTime.now()));
//
//        JournalResponse response = new JournalResponse();
//        response.setTitle("abc");
//        response.setContent("about wings of fire");
//        response.setId(1L);;
//        response.setCreatedAt(LocalDate.now());
//
//        when(modelMapper.map(journals.get(0), JournalResponse.class)).thenReturn(response);
//
//        //When
//        when(journalRepository.findAll()).thenReturn(journals);
//
//        List<JournalResponse> result = journalService.GetJournalList();
//
//        // Assert
//        assertEquals(1, result.size());
//        assertEquals(1L, result.get(0).getId());
//        assertEquals("abc", result.get(0).getTitle());
//        //assertEquals("", result.get(0).);
//    }

}

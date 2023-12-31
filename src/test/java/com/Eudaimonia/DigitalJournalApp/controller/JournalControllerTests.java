package com.Eudaimonia.DigitalJournalApp.controller;

import com.Eudaimonia.DigitalJournalApp.contract.request.JournalRequest;
import com.Eudaimonia.DigitalJournalApp.contract.response.JournalResponse;
import com.Eudaimonia.DigitalJournalApp.contract.response.PaginationResponse;
import com.Eudaimonia.DigitalJournalApp.model.Journal;
import com.Eudaimonia.DigitalJournalApp.service.JournalService;
import com.Eudaimonia.DigitalJournalApp.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
public class JournalControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JournalService journalService;
    @MockBean
    private UserService userService;

    @Test
    void testCreateJournal() throws  Exception {
        String path = "/journal/create";
        Journal entity = new Journal();
        entity.setTitle("abc");
        entity.setContent("about wings of fire");
        entity.setCategory("Books");
        entity.setUpdatedAt(LocalDateTime.now());
        entity.setId(1L);
        JournalRequest request = new JournalRequest();

        //Given
        when(journalService.createJournal(request)).thenReturn(1L);

        ObjectMapper objectMapper = new ObjectMapper();
        String requests = objectMapper.writeValueAsString(request);

        //Then
        mockMvc.perform(post(path)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requests))
                .andDo(print())

                //when
                .andExpect(status().isOk());
    }

    @Test
    void testGetJournalById() throws Exception{
        String path = "/journal/1";
        Long id = 1L;
        JournalResponse response = new JournalResponse();
        response.setTitle("abc");
        response.setContent("about wings of fire");
        response.setId(id);;
        response.setCreatedAt(LocalDate.now());

        //when
        when(journalService.GetJournalById(id)).thenReturn(response);
        //Given
        mockMvc.perform(get(path))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("abc")));
    }

    @Test
    void testRemoveJournal() throws Exception{
        String path = "/journal/1";
        Long id =1L;
        doNothing().when(journalService).RemoveJournalById(id);

        mockMvc.perform(delete(path))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateJournal() throws Exception{
        String path = "/journal/1";
        Journal entity = new Journal();
        entity.setTitle("abc");
        entity.setContent("about wings of fire");
        entity.setId(1L);
        JournalRequest request = new JournalRequest();
        request.setTitle("abc");
        request.setContent("about wings of fire");

        JournalResponse response = new JournalResponse();
        response.setTitle("abc");
        response.setContent("about wings of fire");
        response.setId(1L);;
        response.setCreatedAt(LocalDate.now());
        response.setUpdatedAt(LocalDateTime.now());

        //when
        when(journalService.UpdateJournalById(1L,request)).thenReturn(response);
        //Given
        ObjectMapper objectMapper = new ObjectMapper();
        String requests = objectMapper.writeValueAsString(request);

        //Then
        mockMvc.perform(put(path)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requests))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateDelete() throws Exception{
        String path = "/journal/1";

        //when
        doNothing().when(journalService).SoftDeleteJournalById(1L);
        //Then
        mockMvc.perform(patch(path))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void testRestoreJournal() throws Exception{
        String path = "/journal/1/restore";

        //when
        doNothing().when(journalService).restoreJournalById(1L);
        //Then
        mockMvc.perform(patch(path))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void testGetJournalList() throws Exception{
        String path = "/journal/list";
        Long id = 1L;
        JournalResponse response = new JournalResponse();
        response.setTitle("abc");
        response.setContent("about wings of fire");
        response.setId(id);;
        response.setCreatedAt(LocalDate.now());
        int size = 0;
        int page = 1;

        //when
        when(journalService.getJournalList(size,page)).thenReturn((PaginationResponse) List.of(response));
        //Then
        mockMvc.perform(get(path))
                .andDo(print())

                //when
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title", is("abc")))
                .andExpect(jsonPath("$[0].id", is(1)));
    }

    @Test
    void testGetTrashJournalList() throws Exception{
        String path = "/journal/trash-list";
        Long id = 1L;
        JournalResponse response = new JournalResponse();
        response.setTitle("abc");
        response.setContent("about wings of fire");
        response.setId(id);;
        response.setCreatedAt(LocalDate.now());
        response.setUpdatedAt(LocalDateTime.now());

        //when
        when(journalService.getJournalTrashList()).thenReturn(List.of(response));
        //Then
        mockMvc.perform(get(path))
                .andDo(print())

                //when
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title", is("abc")))
                .andExpect(jsonPath("$[0].id", is(1)));
    }
}

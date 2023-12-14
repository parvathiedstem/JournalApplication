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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class JournalServiceTests {
    private JournalRepository journalRepository;
    private CategoryRepository categoryRepository;
    private JournalService journalService;
    private UserRepository userRepository;
    private ModelMapper modelMapper;
    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @BeforeEach
    public void init(){
        MockitoAnnotations.openMocks(this);
        journalRepository = Mockito.mock(JournalRepository.class);
        categoryRepository = Mockito.mock(CategoryRepository.class);
        userRepository = Mockito.mock(UserRepository.class);
        modelMapper = Mockito.mock(ModelMapper.class);
        journalService = new JournalService(journalRepository,categoryRepository,userRepository, modelMapper);
    }

    @Test
    void testCreateJournal() throws IOException {
        Journal entity = new Journal();
        entity.setTitle("abc");
        entity.setContent("about wings of fire");
        entity.setCreatedAt(LocalDate.now());
        entity.setCategory("Books");
        entity.setId(1L);
        Category category = new Category();
        category.setName("Books");
        JournalRequest request = new JournalRequest();

        UserInfoUserDetails userDetails = new UserInfoUserDetails();
        userDetails.setId(1L);

        User mockUser = new User();
        mockUser.setId(1L);

        // Mocking SecurityContext
        SecurityContextHolder.setContext(securityContext);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        Mockito.when(authentication.getPrincipal()).thenReturn(userDetails);
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));

        when(categoryRepository.findByName(request.getCategory())).thenReturn(category);
        when(journalRepository.save(any())).thenReturn(entity);
        long id = journalService.createJournal(request);

        assertEquals(id,1L);
    }

    @Test
    void testGetJournalById() throws IOException {
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
    void testUpdateJournal() throws IOException {
        User user = new User();
        user.setId(1L);
        Journal entity = new Journal();
        entity.setTitle("abc");
        entity.setContent("about wings of fire");
        entity.setCreatedAt(LocalDate.now());
        entity.setUpdatedAt(LocalDateTime.now());
        entity.setUser(user);
        entity.setId(1L);
        JournalRequest request = new JournalRequest();

        JournalResponse response = new JournalResponse();
        response.setTitle("abc");
        response.setContent("about wings of fire");
        response.setId(1L);;
        response.setCreatedAt(LocalDate.now());

        UserInfoUserDetails userDetails = new UserInfoUserDetails();
        userDetails.setId(1L);

        // Mocking SecurityContext
        SecurityContextHolder.setContext(securityContext);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        Mockito.when(authentication.getPrincipal()).thenReturn(userDetails);

        when(journalRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(journalRepository.save(any())).thenReturn(entity);
        when(modelMapper.map(entity, JournalResponse.class)).thenReturn(response);

        JournalResponse result = journalService.UpdateJournalById(1L, request);

        assertEquals(result.getId(),1L);
    }

    @Test
    void testUpdateSoftDeleteJournal() throws IOException {
        User user = new User();
        user.setId(1L);
        Journal entity = new Journal();
        entity.setTitle("abc");
        entity.setContent("about wings of fire");
        entity.setCreatedAt(LocalDate.now());
        entity.setUpdatedAt(LocalDateTime.now());
        entity.setDeleted(false);
        entity.setUser(user);
        entity.setId(1L);
        JournalResponse response = new JournalResponse();
        response.setDeleted(true);
        response.setUpdatedAt(LocalDateTime.now());

        UserInfoUserDetails userDetails = new UserInfoUserDetails();
        userDetails.setId(1L);

        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Authentication authentication = Mockito.mock(Authentication.class);

        // Mocking SecurityContext
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        when(journalRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(journalRepository.save(any())).thenReturn(entity);
        when(modelMapper.map(entity, JournalResponse.class)).thenReturn(response);

        journalService.SoftDeleteJournalById(1L);

        Mockito.verify(journalRepository).save(entity);
    }

    @Test
    void testDeleteJournal() throws IOException {
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Authentication authentication = Mockito.mock(Authentication.class);
        UserInfoUserDetails userDetails = new UserInfoUserDetails();
        userDetails.setId(1L);

        Journal journal = new Journal();
        journal.setDeleted(true);

        UserInfoUserDetails journalUserDetails = new UserInfoUserDetails();
        journalUserDetails.setId(1L);
        User user = new User();
        user.setId(1L);
        journal.setUser(user); // Assuming there's a setUser method in your Journal class
        journal.setDeleted(true);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        SecurityContextHolder.setContext(securityContext);
        when(journalRepository.findById(1L)).thenReturn(Optional.of(journal));
        doNothing().when(journalRepository).delete(journal);

        journalService.RemoveJournalById(1L);

        verify(journalRepository, times(1)).findById(1L);
        verify(journalRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testListJournals() throws IOException {
        // Given
        List<Journal> journals = new ArrayList<>();
        journals.add(new Journal());

        JournalResponse response = new JournalResponse();
        response.setTitle("abc");
        response.setContent("about wings of fire");
        response.setId(1L);;
        response.setCreatedAt(LocalDate.now());
        int size = 0;
        int page = 1;

        when(modelMapper.map(journals.get(0), JournalResponse.class)).thenReturn(response);

        //When
        when(journalRepository.findAll()).thenReturn(journals);

        PaginationResponse result = journalService.getJournalList(size,page);

        // Assert
        assertEquals(1, result.getTotalItems());
        assertEquals(1L, result.getPageNumber());
    }

    @Test
    public void testListTrashJournals() throws IOException {
        // Given
        List<Journal> journals = new ArrayList<>();
        journals.add(new Journal());

        JournalResponse response = new JournalResponse();
        response.setTitle("abc");
        response.setContent("about wings of fire");
        response.setId(1L);;
        response.setCreatedAt(LocalDate.now());
        response.setUpdatedAt(LocalDateTime.now());
        response.setDeleted(true);

        //When
        when(journalRepository.findByDeleted(true)).thenReturn(journals);
        when(modelMapper.map(journals.get(0), JournalResponse.class)).thenReturn(response);
        List<JournalResponse> result = journalService.getJournalTrashList();
        // Assert
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals("abc", result.get(0).getTitle());
    }

    @Test
    public void testSearchList() throws IOException {
        // Arrange
        int page = 0;
        int size = 10;
        String search = "books";

        Journal journal = new Journal();
        journal.setId(1L);
        journal.setTitle("wings of fire");
        journal.setCategory("books");
        journal.setDeleted(false);

        List<Journal> journals = List.of(journal);

        Pageable pageable = PageRequest.of(page, size);
        Page<Journal> journalPage = new PageImpl<>(journals, pageable, journals.size());

        when(journalRepository.findAll()).thenReturn(journals);
        when(journalRepository.findByCategoryAndDeletedFalse(eq(search), any(Pageable.class))).thenReturn(journalPage);
        when(modelMapper.map(any(), eq(JournalResponse.class))).thenReturn(new JournalResponse());
        JournalResponse response = new JournalResponse();
        // Act
        JournalResponse result = (JournalResponse) journalService.searchList(search);

        // Assert
        assertEquals(1, result.getId());
    }

}

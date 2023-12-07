package com.Eudaimonia.DigitalJournalApp.controller;

import com.Eudaimonia.DigitalJournalApp.service.CategoryService;
import com.Eudaimonia.DigitalJournalApp.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
public class CategoryControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;
    @MockBean
    private UserService userService;

    @Test
    void testCreateCategory() throws  Exception {
        String path = "/category/create";
        String category = "Books";

        //Given
        when(categoryService.createCategory(category)).thenReturn(1L);

        //Then
        mockMvc.perform(post(path)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(category))
                .andDo(print())

                //when
                .andExpect(status().isOk());
    }

    @Test
    void testGetJournalList() throws Exception{
        String path = "/category/list";
        List<String>categories = new ArrayList<>();
        categories.add("Books");

        //when

        when(categoryService.getCategoryList()).thenReturn(categories);
        //Then
        mockMvc.perform(get(path))
                .andDo(print())

                //when
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]", is("Books")));
    }
}

package com.Eudaimonia.DigitalJournalApp.service;

import com.Eudaimonia.DigitalJournalApp.model.Category;
import com.Eudaimonia.DigitalJournalApp.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class CategoryServiceTests {
    private CategoryRepository categoryRepository;
    private CategoryService categoryService;

    @BeforeEach
    public void init(){
        MockitoAnnotations.openMocks(this);
        categoryRepository = Mockito.mock(CategoryRepository.class);
        categoryService = new CategoryService(categoryRepository);
    }

    @Test
    void testCreateCategory() throws IOException {

        Category category = new Category();
        category.setName("Books");
        category.setId(1L);
        when(categoryRepository.save(any())).thenReturn(category);
        long id = categoryService.createCategory("Books");

        assertEquals(id,1L);
    }

    @Test
    public void testListCategories() throws IOException {
        // Given
        Category category = new Category();
        category.setName("Books");
        category.setId(1L);

        //When
        when(categoryRepository.findAll()).thenReturn((List.of(category)));

        List<String> result = categoryService.getCategoryList();

        // Assert
        assertEquals(1, result.size());
        assertEquals("Books", result.get(0));
    }
}

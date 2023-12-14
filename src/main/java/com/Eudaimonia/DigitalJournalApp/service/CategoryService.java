package com.Eudaimonia.DigitalJournalApp.service;

import com.Eudaimonia.DigitalJournalApp.model.Category;
import com.Eudaimonia.DigitalJournalApp.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public Long createCategory(String category) {
        Category categories = categoryRepository.findByName(category);
        if (categories == null) {
            Category response = Category.builder()
                    .name(category)
                    .build();
            response = categoryRepository.save(response);
            return response.getId();
        }
        else
        {
            throw new RuntimeException("category already exists");
        }
}

    public List<String> getCategoryList() throws IOException {
        List<Category> categories = categoryRepository.findAll();

        return categories.stream()
                .map(Category::getName)
                .collect(Collectors.toList());
    }
}

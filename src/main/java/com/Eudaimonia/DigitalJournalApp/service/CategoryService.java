package com.Eudaimonia.DigitalJournalApp.service;

import com.Eudaimonia.DigitalJournalApp.model.Category;
import com.Eudaimonia.DigitalJournalApp.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public Long createCategory(String category){
        Category response = Category.builder()
                .name(category)
                .build();
        response = categoryRepository.save(response);
        return response.getId();
    }
}

package com.Eudaimonia.DigitalJournalApp.service;

import com.Eudaimonia.DigitalJournalApp.contract.Response.JournalResponse;
import com.Eudaimonia.DigitalJournalApp.model.Category;
import com.Eudaimonia.DigitalJournalApp.model.Journal;
import com.Eudaimonia.DigitalJournalApp.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    public Long createCategory(String category)throws IOException{
        Category response = Category.builder()
                .name(category)
                .build();
        response = categoryRepository.save(response);
        return response.getId();
    }

    public List<String> getCategoryList() throws IOException {
        List<Category> categories = categoryRepository.findAll();

        // Assuming Category has a method to get the name, adjust it accordingly
        return categories.stream()
                .map(Category::getName)
                .collect(Collectors.toList());
    }
}

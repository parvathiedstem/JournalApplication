package com.Eudaimonia.DigitalJournalApp.controller;

import com.Eudaimonia.DigitalJournalApp.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/category")
@CrossOrigin(origins = "http://127.0.0.1:5173/")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping("/create")
    public ResponseEntity<Long> createCategory(@RequestBody String category) {
        Long id = categoryService.createCategory(category);
        return ResponseEntity.ok(id);
    }
}
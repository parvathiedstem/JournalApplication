package com.Eudaimonia.DigitalJournalApp.controller;

import com.Eudaimonia.DigitalJournalApp.model.Category;
import com.Eudaimonia.DigitalJournalApp.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/category")
@CrossOrigin(origins = "http://127.0.0.1:5173/")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping("/create")
    public ResponseEntity<?> createCategory(@RequestBody String category) {
        try{
            Long id = categoryService.createCategory(category);
            return ResponseEntity.ok(id);
        }
        catch (IOException e) {
            return ResponseEntity.status(500).body("Error while creating journal " + e.getMessage());
        }
    }

    @GetMapping("/list")
    public ResponseEntity<List<?>> getJournalList(){
        try {
            return ResponseEntity.ok(categoryService.getCategoryList());
        }
        catch (IOException e) {
            return ResponseEntity.status(500).body(Collections.singletonList((Category) Collections.singletonList("Error while retrieving data " + e.getMessage())));
        }
    }
}
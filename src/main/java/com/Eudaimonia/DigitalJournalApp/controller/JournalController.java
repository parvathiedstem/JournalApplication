package com.Eudaimonia.DigitalJournalApp.controller;

import com.Eudaimonia.DigitalJournalApp.contract.Request.JournalRequest;
import com.Eudaimonia.DigitalJournalApp.contract.Response.JournalResponse;
import com.Eudaimonia.DigitalJournalApp.service.JournalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/journal")
@CrossOrigin(origins = "http://127.0.0.1:5173/")
@RequiredArgsConstructor
public class JournalController {
    private final JournalService journalService;

    @PostMapping("/create")
    public ResponseEntity<Long> createJournal(@RequestBody JournalRequest request){
        Long id = journalService.createJournal(request);
        return ResponseEntity.ok(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<JournalResponse> getJournal(@PathVariable Long id){

        return ResponseEntity.ok(journalService.GetJournalById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<JournalResponse> updateJournal(@PathVariable Long id, @RequestBody JournalRequest request){

        return ResponseEntity.ok(journalService.UpdateJournalById(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> removeJournal(@PathVariable Long id){
        journalService.RemoveJournalById(id);
        return ResponseEntity.ok("successfully deleted");
    }

    @GetMapping("/list")
    public ResponseEntity<List<?>> getJournalList(@RequestParam int page, @RequestParam int size){
        try {
            return ResponseEntity.ok(journalService.GetJournalList(page, size));
        }
        catch (IOException e) {
            return ResponseEntity.status(500).body(Collections.singletonList("Error while retrieving data " + e.getMessage()));
        }
    }
}

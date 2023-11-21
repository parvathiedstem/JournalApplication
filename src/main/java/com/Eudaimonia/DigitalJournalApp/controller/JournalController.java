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
    public ResponseEntity<?> createJournal(@RequestBody JournalRequest request){
        try {
            Long id = journalService.createJournal(request);
            return ResponseEntity.ok(id);
        }
        catch (IOException e) {
            return ResponseEntity.status(500).body("Error while creating journal " + e.getMessage());
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<JournalResponse> getJournalById(@PathVariable Long id){
        try {
            return ResponseEntity.ok(journalService.GetJournalById(id));
        }
        catch (IOException e) {
            return ResponseEntity.status(500).body((JournalResponse) Collections.singletonList("Error while fetching data " + e.getMessage()));
        }

    }

    @PutMapping("/{id}")
    public ResponseEntity<JournalResponse> updateJournal(@PathVariable Long id, @RequestBody JournalRequest request){
        try {
            return ResponseEntity.ok(journalService.UpdateJournalById(id, request));
        }
        catch (IOException e) {
            return ResponseEntity.status(500).body((JournalResponse) Collections.singletonList("Error while fetching data " + e.getMessage()));
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> softDeleteJournal(@PathVariable Long id){
        try {
            journalService.SoftDeleteJournalById(id);
            return ResponseEntity.ok("successfully deleted");
        }
        catch (IOException e) {
            return ResponseEntity.status(500).body("Error while soft deleting journal" + e.getMessage());
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> removeJournal(@PathVariable Long id){
        try {
            journalService.RemoveJournalById(id);
            return ResponseEntity.ok("successfully deleted");
        }
        catch (IOException e) {
            return ResponseEntity.status(500).body("Error while deleting journal" + e.getMessage());
        }

    }

    @PatchMapping("/{id}/restore")
    public ResponseEntity<String> restoreDeletedJournal(@PathVariable Long id){
        try {
            journalService.restoreJournalById(id);
            return ResponseEntity.ok("successfully restored");
        }
        catch (IOException e) {
            return ResponseEntity.status(500).body("Error while restoring journal" + e.getMessage());
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<?>> searchJournalList(@RequestParam int page, @RequestParam int size,@RequestParam(required = false) String searchBy){
        try {
            return ResponseEntity.ok(journalService.searchList(page, size,searchBy));
        }
        catch (IOException e) {
            return ResponseEntity.status(500).body(Collections.singletonList("Error while retrieving data " + e.getMessage()));
        }
    }

    @GetMapping("/list")
    public ResponseEntity<List<?>> getJournalList(){
        try {
            return ResponseEntity.ok(journalService.getJournalList());
        }
        catch (IOException e) {
            return ResponseEntity.status(500).body(Collections.singletonList("Error while retrieving data " + e.getMessage()));
        }
    }
}

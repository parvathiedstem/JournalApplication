package com.Eudaimonia.DigitalJournalApp.controller;

import com.Eudaimonia.DigitalJournalApp.contract.request.JournalRequest;
import com.Eudaimonia.DigitalJournalApp.contract.response.JournalResponse;
import com.Eudaimonia.DigitalJournalApp.service.JournalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/journal")
@RequiredArgsConstructor
public class JournalController {
    private final JournalService journalService;

    @PostMapping("/create")
    public ResponseEntity<Long> createJournal(@Validated @RequestBody JournalRequest request) {
         Long id = journalService.createJournal(request);
         return ResponseEntity.ok(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<JournalResponse> getJournalById(@PathVariable Long id){
        return ResponseEntity.ok(journalService.GetJournalById(id));

    }

    @PutMapping("/{id}")
    public ResponseEntity<JournalResponse> updateJournal(@PathVariable Long id, @RequestBody JournalRequest request){
        return ResponseEntity.ok(journalService.UpdateJournalById(id, request));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> softDeleteJournal(@PathVariable Long id){
        journalService.SoftDeleteJournalById(id);
        return ResponseEntity.ok("successfully deleted");
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> removeJournal(@PathVariable Long id){
        journalService.RemoveJournalById(id);
        return ResponseEntity.ok("successfully deleted");

    }

    @PatchMapping("/{id}/restore")
    public ResponseEntity<String> restoreDeletedJournal(@PathVariable Long id){
        journalService.restoreJournalById(id);
        return ResponseEntity.ok("successfully restored");
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchJournalList(@RequestParam int page, @RequestParam int size,@RequestParam(required = false) String searchBy){
        return ResponseEntity.ok(journalService.searchList(page, size,searchBy));
    }

    @GetMapping("/list")
    public ResponseEntity<List<?>> getJournalList(){
        return ResponseEntity.ok(journalService.getJournalList());
    }

    @GetMapping("/trash-list")
    public ResponseEntity<List<?>> getJournalTrashList(){
        return ResponseEntity.ok(journalService.getJournalTrashList());
    }
}

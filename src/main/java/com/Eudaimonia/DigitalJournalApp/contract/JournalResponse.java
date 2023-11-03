package com.Eudaimonia.DigitalJournalApp.contract;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class JournalResponse {
    private Long id;
    private String title;
    private String content;
    private LocalDate createdAt;
    private LocalDateTime updatedAt;
}

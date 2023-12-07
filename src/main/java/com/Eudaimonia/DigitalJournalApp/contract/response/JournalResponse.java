package com.Eudaimonia.DigitalJournalApp.contract.response;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JournalResponse {
    private Long id;
    private String title;
    private String content;
    private LocalDate createdAt;
    private LocalDateTime updatedAt;
    private String category;
    private boolean deleted;
}

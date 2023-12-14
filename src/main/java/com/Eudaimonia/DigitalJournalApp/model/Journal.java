package com.Eudaimonia.DigitalJournalApp.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Journal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @Lob
    private String content;
    private LocalDate createdAt;
    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;
    private LocalDateTime updatedAt;
    private String category;
    private boolean deleted;
}


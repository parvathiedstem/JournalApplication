package com.Eudaimonia.DigitalJournalApp.contract;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class JournalRequest {
    private String title;
    private String content;
}

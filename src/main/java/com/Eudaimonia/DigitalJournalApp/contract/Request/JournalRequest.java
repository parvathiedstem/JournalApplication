package com.Eudaimonia.DigitalJournalApp.contract.Request;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class JournalRequest {
    private String title;
    private String content;
}

package com.Eudaimonia.DigitalJournalApp.contract.Response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {
    private String name;
    private String token;
}

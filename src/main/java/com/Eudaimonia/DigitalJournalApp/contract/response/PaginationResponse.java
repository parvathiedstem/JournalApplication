package com.Eudaimonia.DigitalJournalApp.contract.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaginationResponse {
    private List<JournalResponse> data;
    private int pageNumber;
    private int pageSize;
    private long totalItems;
}

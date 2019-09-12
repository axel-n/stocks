package com.bcs.stocks.models;

import lombok.*;

import java.util.List;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StockRequest {
    private List<StockDto> stocks;
}

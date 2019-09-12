package com.bcs.stocks.models;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StockResponse {
    private List<StocksStatistic> allocations;
    private BigDecimal value;
}

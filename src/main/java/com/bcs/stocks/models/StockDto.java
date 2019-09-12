package com.bcs.stocks.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockDto {
    private String symbol;
    private Long volume;
    private BigDecimal price;
}

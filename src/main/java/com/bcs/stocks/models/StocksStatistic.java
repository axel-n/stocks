package com.bcs.stocks.models;

import com.bcs.stocks.models.dict.Sector;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StocksStatistic {
    private Sector sector;
    private BigDecimal assetValue;
    private BigDecimal proportion;
}

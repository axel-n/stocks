package com.bcs.stocks.stockService;

import com.bcs.stocks.models.dict.Sector;

public interface StockService {
    Sector getSectorByStock(String stock);
}

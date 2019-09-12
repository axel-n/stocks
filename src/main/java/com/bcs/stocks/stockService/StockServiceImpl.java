package com.bcs.stocks.stockService;

import com.bcs.stocks.models.dict.Sector;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Service
public class StockServiceImpl implements StockService {

    private Set<String> technologyStocks;
    private Set<String> healthCareStocks;

    private StockServiceImpl() {
        technologyStocks = new HashSet<>(Set.of("AAPL", "HOG", "MDSO"));
        healthCareStocks = new HashSet<>(Set.of("IDRA", "MRSN"));
    }

    @Override
    public Sector getSectorByStock(String stock) {
        if (technologyStocks.contains(stock)) {
            return Sector.Technology;
        } else if (healthCareStocks.contains(stock)) {
            return Sector.Healthcare;
        } else {
            return null;
        }
    }
}

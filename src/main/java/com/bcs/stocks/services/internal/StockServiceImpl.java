package com.bcs.stocks.services.internal;

import com.bcs.stocks.models.StockDto;
import com.bcs.stocks.models.StockRequest;
import com.bcs.stocks.models.StockResponse;
import com.bcs.stocks.models.StocksStatistic;
import com.bcs.stocks.models.dict.Sector;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Slf4j
@Service
public class StockServiceImpl implements StockService {

    private Set<String> technologyStocks;
    private Set<String> healthCareStocks;

    private StockServiceImpl() {
        technologyStocks = new HashSet<>(Set.of("AAPL", "HOG", "MDSO"));
        healthCareStocks = new HashSet<>(Set.of("IDRA", "MRSN"));
    }

    private Sector getSectorByStock(String stock) {
        log.info("getSectorByStock. stock {}", stock);
        if (technologyStocks.contains(stock)) {
            return Sector.Technology;
        } else if (healthCareStocks.contains(stock)) {
            return Sector.Healthcare;
        } else {
            return null;
        }
    }

    @Override
    public Mono<StockResponse> getStatisticByParams(StockRequest request) {
        if (request != null && request.getStocks() != null) {

            return Flux.just(request.getStocks())
                    .flatMap(Flux::fromIterable)
                    .map(StockDto::getSymbol)
                    .collectList()
                    .flatMap(this::getPriceByListSymbols)
                    .flatMap(mapWithPrice -> mergePriceAndVolume(mapWithPrice, request))
                    .map(this::mergeStockStatisticByCategory);

        } else {
            return Mono.empty();
        }
    }

    private Mono<Map<String, Object>> getPriceByListSymbols(List<String> listSymbols) {

        log.info("start getPriceByListSymbols. get listSymbols {}", listSymbols);

        // TODO получать реальные цифры

        // mock
        Map<String, Object> response = new HashMap<>();
        listSymbols.forEach(symbol -> {
            Map<String, BigDecimal> symbolData = new HashMap<>();
            symbolData.put("price", new BigDecimal("100.00"));
            response.put(symbol, symbolData);
        });

        return Mono.just(response);
    }

    private Mono<StockRequest> mergePriceAndVolume(Map<String, Object> mapWithPrice, StockRequest request) {

        log.info("start mergePriceAndVolume. get mapWithPrice {}, request {}", mapWithPrice, request);

        return Flux.just(request.getStocks())
                .flatMap(Flux::fromIterable)
                .map(stockDto -> {
                    Map<String, BigDecimal> symbolData = (Map<String, BigDecimal>) mapWithPrice.get(stockDto.getSymbol());
                    BigDecimal priceByOneStock = symbolData.get("price");
                    BigDecimal priceByVolume = priceByOneStock.multiply(BigDecimal.valueOf(stockDto.getVolume()));
                    stockDto.setPrice(priceByVolume);
                    return stockDto;
                })
                .collectList()
                .map(list -> {
                    log.info("list {}", list);
                    request.setStocks(list);
                    return request;
                });
    }

    private StockResponse mergeStockStatisticByCategory(StockRequest processedRequest) {
        log.info("start mergeStockStatisticByCategory. get processedRequest {}", processedRequest);

        Map<Sector, BigDecimal> valueBySector = new HashMap<>();

        for (StockDto stockDto : processedRequest.getStocks()) {
            Sector sector = getSectorByStock(stockDto.getSymbol());

            if (sector != null) {
                BigDecimal oldValue = valueBySector.get(sector);

                BigDecimal newValue = stockDto.getPrice();
                if (oldValue != null) {
                    newValue = newValue.add(oldValue);
                }

                valueBySector.put(sector, newValue);
            }
        }

        BigDecimal totalValue = BigDecimal.ZERO;
        for (Map.Entry<Sector, BigDecimal> entrySector : valueBySector.entrySet()) {
            totalValue = totalValue.add(entrySector.getValue());
        }

        List<StocksStatistic> statistics = new ArrayList<>();
        for (Map.Entry<Sector, BigDecimal> entrySector : valueBySector.entrySet()) {

            BigDecimal valueSector = entrySector.getValue();
            BigDecimal proportionSector = valueSector.divide(totalValue, RoundingMode.HALF_UP);
            proportionSector = proportionSector.setScale(3, RoundingMode.HALF_EVEN);

            StocksStatistic statistic = new StocksStatistic();
            statistic.setSector(entrySector.getKey());
            statistic.setAssetValue(valueSector);
            statistic.setProportion(proportionSector);

            statistics.add(statistic);
        }

        StockResponse response = new StockResponse();
        response.setAllocations(statistics);
        response.setValue(totalValue);
        return response;
    }
}

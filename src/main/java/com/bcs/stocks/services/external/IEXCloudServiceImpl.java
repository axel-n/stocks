package com.bcs.stocks.services.external;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class IEXCloudServiceImpl implements IEXCloudService {

    private final WebClient webClient = WebClient.create();

    @Value("${apiRootPath}")
    private String apiRootPath;

    @Value("${token}")
    private String token;

    @Override
    public Mono<Map<String, Object>> getDataByStocks(List<String> listSymbols) {

        log.info("getDataByStocks start. listSymbols {}", listSymbols);

        String url = String.format("%s/stock/market/batch?types=price&symbols=%s&token=%s",
                apiRootPath, String.join(",", listSymbols), token);

        return webClient.get()
                .uri(url)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .filter(response -> !response.statusCode().isError())
                .flatMap(body -> body.bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
                }));


    }
}

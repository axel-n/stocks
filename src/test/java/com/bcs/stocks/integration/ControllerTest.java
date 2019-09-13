package com.bcs.stocks.integration;

import com.bcs.stocks.models.StockResponse;
import com.bcs.stocks.models.StocksStatistic;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.util.List;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ControllerTest {

    @Autowired
    private WebTestClient client;

    @Value("${spring.data.rest.base-path}")
    private String rootApi;

    // TODO переписать через загрузку из файла
    private final String validJson = " {\n" +
            "    \"stocks\":[\n" +
            "      {\n" +
            "         \"symbol\":\"AAPL\",\n" +
            "         \"volume\":50\n" +
            "      },\n" +
            "      {\n" +
            "         \"symbol\":\"HOG\",\n" +
            "         \"volume\":10\n" +
            "      },\n" +
            "      {\n" +
            "         \"symbol\":\"MDSO\",\n" +
            "         \"volume\":1\n" +
            "      },\n" +
            "      {\n" +
            "         \"symbol\":\"IDRA\",\n" +
            "         \"volume\":1\n" +
            "      },\n" +
            "      {\n" +
            "         \"symbol\":\"MRSN\",\n" +
            "         \"volume\":1000\n" +
            "      }\n" +
            "    ]\n" +
            "}";

    // TODO переписать через загрузку из файла
    private final String notValidSymbols = " {\n" +
            "    \"stocks\":[\n" +
            "      {\n" +
            "         \"symbol\":\"AAAAAA\",\n" +
            "         \"volume\":50\n" +
            "      },\n" +
            "      {\n" +
            "         \"symbol\":\"BBBB\",\n" +
            "         \"volume\":10\n" +
            "      },\n" +
            "      {\n" +
            "         \"symbol\":\"CCCCCC\",\n" +
            "         \"volume\":1\n" +
            "      },\n" +
            "    ]\n" +
            "}";

    @Test
    public void validSymbols() {
        EntityExchangeResult<StockResponse> response = client.post()
                .uri(rootApi + "/stocks/statistic")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(BodyInserters.fromObject(validJson))
                .exchange()
                .expectStatus().isOk()
                .expectBody(StockResponse.class)
                .returnResult();

        StockResponse stockResponse = response.getResponseBody();

        assertNotNull(stockResponse);
        assertNotNull(stockResponse.getValue());
        assertNotNull(stockResponse.getAllocations());

        List<StocksStatistic> items = stockResponse.getAllocations();
        assertNotNull(items.get(0).getAssetValue());
        assertNotNull(items.get(0).getProportion());
        assertNotNull(items.get(0).getSector());
    }

    @Test
    public void notValidSymbols() {
        client.post()
                .uri(rootApi + "/stocks/statistic")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(BodyInserters.fromObject(notValidSymbols))
                .exchange()
                .expectStatus().is4xxClientError();
    }
}

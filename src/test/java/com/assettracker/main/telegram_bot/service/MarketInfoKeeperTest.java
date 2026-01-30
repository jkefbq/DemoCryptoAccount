package com.assettracker.main.telegram_bot.service;

import com.assettracker.main.telegram_bot.menu.asset_list_menu.Coins;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.queryParam;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

public class MarketInfoKeeperTest {

    private final RestTemplate restTemplate = new RestTemplate();
    private final MockRestServiceServer server = MockRestServiceServer.createServer(restTemplate);
    private final MarketInfoKeeper marketInfoKeeper =
            new MarketInfoKeeper("key", "https://test-url", new ObjectMapper(), restTemplate);

    private static String getcoinPricesResponseJson() {
        return "{\n\"bitcoin\":{\n\"usd\":80252.3952382432\n},\n\"pepe\":{\n\"usd\":0.0000010472\n}," +
               "\n\"ethereum\":{\n\"usd\":2717.5739375027\n},\n\"litecoin\":{\n\"usd\":0.9998375927\n}\n}";
    }

    private static String getCoinChangesResponseJson() {
        return "{\n\"bitcoin\":{\n\"usd\":80252.3952382432,\n\"usd_24h_change\":3.42625\n},\n\"pepe\":{\n" +
               "\"usd\":0.0000010472,\n\"usd_24h_change\":0.3825\n},\n\"ethereum\":{\n\"usd\":2717.57347,\n" +
               "\"usd_24h_change\":-2.35263\n},\n\"litecoin\":{\n\"usd\":0.9998375927,\"usd_24h_change\":0.002523\n}\n}";
    }

    @BeforeEach
    public void reset() {
        server.reset();
    }

    @SneakyThrows
    @Test
    public void getCoinPricesTest() {
        Set<Coins> coins = Set.of(Coins.PEPE, Coins.BITCOIN, Coins.ETHEREUM, Coins.LITECOIN);
        String response = getcoinPricesResponseJson();
        server.expect(once(), requestTo(startsWith("https://test-url")))
                .andExpect(method(HttpMethod.GET))
                .andExpect(queryParam("ids",
                        String.join( ",", coins.stream().map(Coins::getIdsName).toList())))
                .andRespond(withSuccess(response, MediaType.APPLICATION_JSON));

        Map<Coins, BigDecimal> coinsPrice = marketInfoKeeper.getCoinPrices(coins);

        assertTrue(coinsPrice.containsKey(Coins.BITCOIN) && coinsPrice.containsKey(Coins.PEPE) &&
                   coinsPrice.containsKey(Coins.ETHEREUM) && coinsPrice.containsKey(Coins.LITECOIN));
        assertEquals(coinsPrice.get(Coins.BITCOIN), BigDecimal.valueOf(80252.3952382432));
    }

    @SneakyThrows
    @Test
    public void getCoinChangesTest() {
        Set<Coins> coins = Set.of(Coins.PEPE, Coins.BITCOIN, Coins.ETHEREUM, Coins.LITECOIN);
        String response = getCoinChangesResponseJson();
        server.expect(once(), requestTo(startsWith("https://test-url")))
                .andExpect(method(HttpMethod.GET))
                .andExpect(queryParam("include_24hr_change", "true"))
                .andExpect(queryParam("ids",
                        String.join( ",", coins.stream().map(Coins::getIdsName).toList())))
                .andRespond(withSuccess(response, MediaType.APPLICATION_JSON));

        Map<Coins, Map.Entry<BigDecimal, BigDecimal>> coinChangePrice = marketInfoKeeper.getCoinChanges(coins);

        assertTrue(coinChangePrice.containsKey(Coins.BITCOIN) && coinChangePrice.containsKey(Coins.PEPE) &&
                   coinChangePrice.containsKey(Coins.ETHEREUM) && coinChangePrice.containsKey(Coins.LITECOIN));
        assertEquals(coinChangePrice.get(Coins.LITECOIN),
                Map.entry(BigDecimal.valueOf(0.002523), BigDecimal.valueOf(0.9998375927)));
    }
}

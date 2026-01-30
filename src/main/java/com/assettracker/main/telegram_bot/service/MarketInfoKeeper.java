package com.assettracker.main.telegram_bot.service;

import com.assettracker.main.telegram_bot.menu.asset_list_menu.Coins;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service
public class MarketInfoKeeper {

    private final String KEY_HEADER = "x-cg-demo-api-key";
    private final String GECKO_KEY;
    private final String SIMPLE_PRICE_URL;
    private final ObjectMapper mapper;
    private final RestTemplate restTemplate;

    public MarketInfoKeeper(
            @Value("${GECKO_KEY}") String GECKO_KEY,
            @Value("${api.url.simple-price}") String SIMPLE_PRICE_URL,
            ObjectMapper mapper, RestTemplate restTemplate
    ) {
        this.GECKO_KEY = GECKO_KEY;
        this.SIMPLE_PRICE_URL = SIMPLE_PRICE_URL;
        this.mapper = mapper;
        this.restTemplate = restTemplate;
    }

    public Map<Coins, BigDecimal> getCoinPrices(Set<Coins> coins) throws JsonProcessingException {
        String coinsIdsString = String.join(",", coins.stream().map(Coins::getIdsName).toList());

        HttpHeaders headers = new HttpHeaders();
        headers.add(KEY_HEADER, GECKO_KEY);
        HttpEntity<String> http = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                SIMPLE_PRICE_URL + "?vs_currencies=usd&precision=10&ids=" + coinsIdsString,
                HttpMethod.GET,
                http,
                String.class
        );
        return toResultCoinPriceMap(response.getBody());
    }

    /** returns Map[Coin, [change(%), price(usd)]] */
    public Map<Coins, Map.Entry<BigDecimal, BigDecimal>> getCoinChanges(Set<Coins> coins) throws JsonProcessingException {
        String coinsIdsString = String.join(",", coins.stream().map(Coins::getIdsName).toList());

        HttpHeaders headers = new HttpHeaders();
        headers.add(KEY_HEADER, GECKO_KEY);
        HttpEntity<String> http = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                SIMPLE_PRICE_URL + "?vs_currencies=usd&precision=10&include_24hr_change=true&ids=" + coinsIdsString,
                HttpMethod.GET,
                http,
                String.class
        );
        return toResultCoinChangeMap(response.getBody());
    }

    private Map<Coins, Map.Entry<BigDecimal, BigDecimal>> toResultCoinChangeMap(
            String json
    ) throws JsonProcessingException {
        Map<Coins, Map.Entry<BigDecimal, BigDecimal>> result = new HashMap<>();

        JsonNode jsonNode = mapper.readTree(json);
        jsonNode.forEachEntry((coinIds, node) -> {
            result.put(
                    Coins.getCoinForIds(coinIds),
                    Map.entry(
                            node.get("usd_24h_change").decimalValue(), node.get("usd").decimalValue()
                    )
            );
        });
        return result;
    }


    private Map<Coins, BigDecimal> toResultCoinPriceMap(String json) throws JsonProcessingException {
        Map<Coins, BigDecimal> result = new HashMap<>();

        JsonNode jsonNode = mapper.readTree(json);
        jsonNode.forEachEntry((coinIds, node) ->
                result.put(Coins.getCoinForIds(coinIds), node.get("usd").decimalValue()));
        return result;
    }
}


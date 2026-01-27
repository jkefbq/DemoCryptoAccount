package com.assettracker.main.telegram_bot.database.service;

import com.assettracker.main.telegram_bot.menu.asset_list_menu.Coins;
import com.assettracker.main.telegram_bot.menu.asset_list_menu.UserCoin;
import com.assettracker.main.telegram_bot.database.dto.BagDto;
import com.assettracker.main.telegram_bot.database.entity.BagEntity;
import com.assettracker.main.telegram_bot.database.mapper.BagMapper;
import com.assettracker.main.telegram_bot.database.repository.BagRepository;
import com.assettracker.main.telegram_bot.service.MarketInfoKeeper;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Service
@AllArgsConstructor
public class BagService {

    private final BagMapper mapper;
    private final ExecutorService es = Executors.newFixedThreadPool(10);
    private static final String CACHE_NAMES = "bags";
    private final BagRepository bagRepo;
    private final MarketInfoKeeper marketInfoKeeper;

    @Transactional
    @CachePut(cacheNames = CACHE_NAMES, key = "#entity.getChatId", unless = "#result == null")
    public BagDto updateBag(BagEntity entity) {
        return mapper.toDto(bagRepo.save(entity));
    }

    public BagDto toDto(BagEntity entity) {
        return mapper.toDto(entity);
    }

    public BagEntity toEntity(BagDto dto) {
        return mapper.toEntity(dto);
    }

    @Transactional
    public BagDto createBag(BagEntity entity) {
        return mapper.toDto(bagRepo.save(entity));
    }

    @Transactional
    @Cacheable(cacheNames = CACHE_NAMES, key = "#chatId", unless = "#result == null")
    public Optional<BagDto> findByChatId(Long chatId) {
        return bagRepo.findByChatId(chatId).map(mapper::toDto);
    }

    @Transactional
    public void deleteAsset(Long chatId, Coins coin) {
        BagDto bag = findByChatId(chatId).orElseThrow();
        bag.getAssets().remove(coin);
        try {
            actualizeBagFields(bag);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("exception for call coinGecko api method");
        }
    }

    @Transactional
    public void addAsset(UserCoin userCoin) {
        log.info("add or update asset {name={}, count={}} into bag with chatId={}",
                userCoin.getCoin().name(), userCoin.getCount(), userCoin.getChatId());
        BagDto bag = findByChatId(userCoin.getChatId()).orElseThrow();
        bag.getAssets().put(userCoin.getCoin(), userCoin.getCount());
        try {
            actualizeBagFields(bag);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("exception for call coinGecko api method");
        }
    }

    @Transactional
    public void actualizeBagFields(BagDto bag) throws IOException, InterruptedException {
        Map<Coins, BigDecimal> coinPrices = marketInfoKeeper.getCoinPrices(bag.getAssets().keySet());

        BigDecimal totalCost = bag.getAssets().entrySet().stream()
                .map(entry -> coinPrices.get(entry.getKey()).multiply(entry.getValue()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        bag.setTotalCost(totalCost);
        bag.setAssetCount(bag.getAssets().size());

        updateBag(mapper.toEntity(bag));
        log.info("update assets: {totalCost:{}, assetCount:{}} in bag with chatId={}", bag.getTotalCost(),
                bag.getAssetCount(), bag.getChatId());
    }

    @SneakyThrows
    @Transactional
    public Map<Coins, Map.Entry<BigDecimal, BigDecimal>> getCoinCountAndPrices(Map<Coins, BigDecimal> coinCount) {
        Map<Coins, Map.Entry<BigDecimal, BigDecimal>> result = new HashMap<>();
        marketInfoKeeper.getCoinPrices(coinCount.keySet()).forEach((coin, price) -> {
            result.put(coin, Map.entry(coinCount.get(coin), price));
        });
        return result;
    }

    @SneakyThrows
    @Transactional
    public Map<Coins, Map.Entry<BigDecimal, BigDecimal>> getCoinChanges(Long chatId) {
        BagDto bag = findByChatId(chatId).orElseThrow();
        return marketInfoKeeper.getCoinChanges(bag.getAssets().keySet());
    }

    @Transactional
    public boolean hasCoin(Long chatId, Coins coin) {
        return findByChatId(chatId).orElseThrow()
                .getAssets().keySet().stream()
                .anyMatch(userCoin -> userCoin == coin);
    }
}

package com.asettracker.tg.main.database.service;

import com.asettracker.tg.main.database.entity.BagEntity;
import com.asettracker.tg.main.database.repository.BagRepository;
import com.asettracker.tg.main.menu.asset_list_menu.UserChoose;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class BagDbService {

    private static final String CACHE_NAMES = "bags";
    private static final int BAG_TTL_MINUTES = 5;
    private final BagRepository bagRepo;

    @CacheEvict(cacheNames = CACHE_NAMES, key = "#bag.getChatId")
    public BagEntity updateBag(BagEntity bag) {
        return bagRepo.save(bag);
    }

    public BagEntity createBag(BagEntity bag) {
        return bagRepo.save(bag);
    }

    @Cacheable(cacheNames = CACHE_NAMES, key = "#chatId")
    public Optional<BagEntity> findBagByChatId(Long chatId) {
        return bagRepo.findByChatId(chatId);
    }

    public void addAsset(UserChoose userChoose, Long chatId) {
        findBagByChatId(chatId).ifPresentOrElse(bag -> {
            bag.getAssets().put(userChoose.getCoinName(), userChoose.getCoinCount());
            updateBag(bag);
            actualizeBagFields(chatId);
        }, () -> {
            throw new NoSuchElementException("BagEntity with chatId:" + chatId + " wasn't found in database");
        });
    }

    public void actualizeBagFields(Long chatId) {
        BagEntity bag = findBagByChatId(chatId).orElseThrow();
        bag.setAssetCount(bag.getAssets().size());

        //todo строка ниже просто считает кол-во, нужно дергать другой сервис
        var totalCost = bag.getAssets().values().stream().mapToDouble(e -> e).sum();
        bag.setTotalCost(new BigDecimal(totalCost));
    }
}

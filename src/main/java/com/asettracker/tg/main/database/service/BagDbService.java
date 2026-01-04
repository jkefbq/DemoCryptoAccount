package com.asettracker.tg.main.database.service;

import com.asettracker.tg.main.database.entity.BagEntity;
import com.asettracker.tg.main.database.repository.BagRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class BagDbService {

    private final BagRepository bagRepo;

    public BagEntity saveBag(BagEntity bag) {
        return bagRepo.save(bag);
    }

    public Optional<BagEntity> findBagByChatId(Long chatId) {
        return bagRepo.findByChatId(chatId);
    }

    public void addAsset(String name, Double count, Long chatId) {
        findBagByChatId(chatId).ifPresentOrElse(b -> {
            b.getAssets().put(name, count);
            saveBag(b);
        }, () -> {
            throw new NoSuchElementException("This bag was not found in the database");
        });
    }

    public void actualizeBagAssets(Long chatId) {
        BagEntity bag = bagRepo.findByChatId(chatId).orElseThrow();
        bag.setAssetCount(bag.getAssets().size());
        //todo строка ниже просто считает кол-во, нужно дергать другой сервис
        var totalCost = bag.getAssets().values().stream().mapToDouble(e -> e).sum();
        bag.setTotalCost(new BigDecimal(totalCost));
    }
}

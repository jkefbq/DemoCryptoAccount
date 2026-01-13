package com.asettracker.tg.main.database.service;

import com.asettracker.tg.main.database.dto.BagDto;
import com.asettracker.tg.main.database.entity.BagEntity;
import com.asettracker.tg.main.database.mapper.BagMapper;
import com.asettracker.tg.main.database.repository.BagRepository;
import com.asettracker.tg.main.menu.asset_list_menu.UserChoose;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class BagDbService {

    private static final String CACHE_NAMES = "bags";
    private final BagMapper mapper;
    private final BagRepository bagRepo;

    @Transactional
    @CachePut(cacheNames = CACHE_NAMES, key = "#entity.getChatId")
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
    @Cacheable(cacheNames = CACHE_NAMES, key = "#chatId")
    public Optional<BagDto> findBagByChatId(Long chatId) {
        return bagRepo.findByChatId(chatId).map(mapper::toDto);
    }

    @Transactional
    public void addAsset(UserChoose userChoose, Long chatId) {
        BagDto bag = findBagByChatId(chatId).orElseThrow();
        bag.getAssets().put(userChoose.getCoinName(), userChoose.getCoinCount());
        updateBag(mapper.toEntity(bag));
//        actualizeBagFields(chatId);
    }

    @Transactional
    public void actualizeBagFields(Long chatId) {
        BagDto bag = findBagByChatId(chatId).orElseThrow();
        bag.setAssetCount(bag.getAssets().size());

        //todo строка ниже просто считает кол-во, нужно дергать другой сервис
        var totalCost = bag.getAssets().values().stream().mapToDouble(BigDecimal::doubleValue).sum();
        bag.setTotalCost(new BigDecimal(totalCost));
    }
}

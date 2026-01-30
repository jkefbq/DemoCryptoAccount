package com.assettracker.main.telegram_bot.database.service;

import com.assettracker.main.telegram_bot.menu.asset_list_menu.UserCoin;
import com.assettracker.main.telegram_bot.database.entity.UserCoinEntity;
import com.assettracker.main.telegram_bot.database.repository.UserCoinRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@AllArgsConstructor
public class AssetService {

    private final UserCoinRepository userCoinRepository;

    @Transactional
    public void saveTmpUserCoin(UserCoin coin) {
        userCoinRepository.save(new UserCoinEntity(coin));
        log.info("save tmp UserCoin={}", coin);
    }

    @Transactional
    public UserCoin getTmpUserCoin(Long chatId) {
        UserCoinEntity entity = userCoinRepository.findByChatId(chatId).orElseThrow();
        return new UserCoin(entity.getId(), entity.getCoin(), entity.getChatId(), entity.getAssetDo());
    }

    @Transactional
    public void deleteTmpUserCoin(Long chatId) {
        userCoinRepository.deleteByChatId(chatId);
        log.info("delete tmp UserCoin with chatId={}", chatId);
    }

    @Transactional
    public boolean isUserWaitingNumber(Long chatId) {
        return userCoinRepository.findByChatId(chatId).isPresent();
    }
}

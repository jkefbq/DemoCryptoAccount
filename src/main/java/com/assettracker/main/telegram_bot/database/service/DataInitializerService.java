package com.assettracker.main.telegram_bot.database.service;

import com.assettracker.main.telegram_bot.dto.UpdateDto;
import com.assettracker.main.telegram_bot.database.dto.BagDto;
import com.assettracker.main.telegram_bot.database.entity.BagEntity;
import com.assettracker.main.telegram_bot.database.entity.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class DataInitializerService {

    private final UserService userService;
    private final BagService bagService;

    @Transactional
    public void initializeUserAndBag(UpdateDto updateDto) {
        UserEntity user = new UserEntity(updateDto);
        BagDto bag = bagService.createBag(new BagEntity(updateDto.getChatId()));
        user.setBag(bagService.toEntity(bag));
        userService.createUser(user);
    }
}

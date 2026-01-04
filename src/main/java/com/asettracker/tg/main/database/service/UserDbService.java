package com.asettracker.tg.main.database.service;

import com.asettracker.tg.main.database.entity.BagEntity;
import com.asettracker.tg.main.database.entity.UserEntity;
import com.asettracker.tg.main.database.repository.UserRepository;
import com.asettracker.tg.main.dto.UserStatus;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class UserDbService {

    private final UserRepository userRepo;

    public UserEntity createUserAndBag(UserEntity user, BagEntity bag) {
        user.setBag(bag);
        return userRepo.save(user);
    }

    public boolean hasUserByChatId(Long chatId) {
        return userRepo.findByChatId(chatId).isPresent();
    }

    public Optional<UserEntity> findByChatId(Long chatId) {
        return userRepo.findByChatId(chatId);
    }

    public void setStatus(Long chatId, UserStatus status) {
        UserEntity user = userRepo.findByChatId(chatId).orElseThrow();
        user.setUserStatus(status);
        userRepo.save(user);
    }
}

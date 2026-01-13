package com.asettracker.tg.main.database.service;

import com.asettracker.tg.main.database.UserStatus;
import com.asettracker.tg.main.database.dto.UserDto;
import com.asettracker.tg.main.database.entity.UserEntity;
import com.asettracker.tg.main.database.mapper.UserMapper;
import com.asettracker.tg.main.database.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class UserDbService {

    private static final String CACHE_NAMES = "users";
    private final UserRepository userRepo;
    private final UserMapper mapper;

    public boolean hasUserByChatId(Long chatId) {
        return userRepo.findByChatId(chatId).isPresent();
    }

    public UserDto createUser(UserEntity entity) {
        return mapper.toDto(userRepo.save(entity));
    }

    @Cacheable(cacheNames = CACHE_NAMES, key = "#chatId")
    public Optional<UserEntity> findByChatId(Long chatId) {
        return userRepo.findByChatId(chatId);
    }

    @CachePut(cacheNames = CACHE_NAMES, key = "#chatId")
    public UserEntity setStatus(Long chatId, UserStatus status) {
        UserEntity user = userRepo.findByChatId(chatId).orElseThrow();
        user.setUserStatus(status);
        return userRepo.save(user);
    }

    @CachePut(cacheNames = CACHE_NAMES, key = "#user.chatId")
    public UserEntity setStatus(UserEntity user, UserStatus status) {
        user.setUserStatus(status);
        return userRepo.save(user);
    }
}

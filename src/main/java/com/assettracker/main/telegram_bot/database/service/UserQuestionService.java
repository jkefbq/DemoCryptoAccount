package com.assettracker.main.telegram_bot.database.service;

import com.assettracker.main.telegram_bot.database.dto.UserQuestionDto;
import com.assettracker.main.telegram_bot.database.entity.UserQuestionEntity;
import com.assettracker.main.telegram_bot.database.mapper.UserQuestionMapper;
import com.assettracker.main.telegram_bot.database.repository.UserQuestionRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional
public class UserQuestionService {

    private final UserQuestionRepository userQuestionRepository;
    private final UserQuestionMapper mapper;
    private final UserService userService;

    public UserQuestionEntity save(UserQuestionDto dto) {
        var entity = mapper.toEntity(dto);
        return userQuestionRepository.save(entity);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<UserQuestionDto> getAllUserQuestions() {
        return userQuestionRepository.findAll().stream()
                .map(mapper::toDto)
                .toList();
    }

    public Optional<UserQuestionDto> findById(UUID id) {
        return userQuestionRepository.findById(id).map(mapper::toDto);
    }

    public void deleteById(UUID id) {
        userQuestionRepository.deleteById(id);
    }

    public List<UserQuestionDto> findByUserId(UUID userId) {
        return userQuestionRepository.findByUserId(userId).stream()
                .map(mapper::toDto)
                .toList();
    }
}

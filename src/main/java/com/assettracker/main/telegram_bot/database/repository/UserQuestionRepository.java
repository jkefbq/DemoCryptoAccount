package com.assettracker.main.telegram_bot.database.repository;

import com.assettracker.main.telegram_bot.database.entity.UserQuestionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserQuestionRepository extends JpaRepository<UserQuestionEntity, UUID> {

    List<UserQuestionEntity> findByUserId(UUID userId);
}

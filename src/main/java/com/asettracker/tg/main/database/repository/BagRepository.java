package com.asettracker.tg.main.database.repository;

import com.asettracker.tg.main.database.entity.BagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BagRepository extends JpaRepository<BagEntity, UUID> {

    Optional<BagEntity> findByChatId(Long chatId);
}

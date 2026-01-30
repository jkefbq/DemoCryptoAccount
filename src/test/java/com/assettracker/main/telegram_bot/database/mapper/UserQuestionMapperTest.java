package com.assettracker.main.telegram_bot.database.mapper;

import com.assettracker.main.telegram_bot.database.dto.UserQuestionDto;
import com.assettracker.main.telegram_bot.database.entity.UserQuestionEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserQuestionMapperTest {

    private final UserQuestionMapper mapper = new UserQuestionMapperImpl();

    @Test
    public void toDtoTest() {
        UserQuestionEntity entity = new UserQuestionEntity(
                UUID.randomUUID(),
                UUID.randomUUID(),
                "question",
                1,
                LocalDateTime.now(),
                true
        );

        UserQuestionDto dto = mapper.toDto(entity);

        assertEquals(entity.getVersion(), dto.getVersion());
        assertEquals(entity.getCreatedAt(), dto.getCreatedAt());
        assertEquals(entity.getUserId(), dto.getUserId());
    }

    @Test
    public void toEntityTest() {
        UserQuestionDto dto = UserQuestionDto.builder()
                .question("question")
                .userId(UUID.randomUUID())
                .version(1)
                .createdAt(LocalDateTime.now())
                .build();

        UserQuestionEntity entity = mapper.toEntity(dto);

        assertEquals(entity.getVersion(), dto.getVersion());
        assertEquals(entity.getCreatedAt(), dto.getCreatedAt());
        assertEquals(entity.getUserId(), dto.getUserId());
    }
}

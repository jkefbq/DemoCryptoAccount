package com.assettracker.main.telegram_bot.database.mapper;

import com.assettracker.main.telegram_bot.database.dto.UserDto;
import com.assettracker.main.telegram_bot.database.entity.UserEntity;
import com.assettracker.main.telegram_bot.database.entity.UserStatus;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserMapperTest {

    private final UserMapper mapper = new UserMapperImpl();

    @Test
    public void toDtoTest() {
        UserEntity entity = new UserEntity();
        entity.setId(UUID.randomUUID());
        entity.setStatus(UserStatus.FREE);
        entity.setUserName("username");

        UserDto dto = mapper.toDto(entity);

        assertEquals(dto.getId(), entity.getId());
        assertEquals(dto.getStatus(), entity.getStatus());
        assertEquals(dto.getUserName(), entity.getUserName());
    }

    @Test
    public void toEntityTest() {
        UserDto dto = UserDto.builder()
                .id(UUID.randomUUID())
                .status(UserStatus.FREE)
                .userName("username")
                .build();

        UserEntity entity = mapper.toEntity(dto);

        assertEquals(dto.getId(), entity.getId());
        assertEquals(dto.getStatus(), entity.getStatus());
        assertEquals(dto.getUserName(), entity.getUserName());
    }
}

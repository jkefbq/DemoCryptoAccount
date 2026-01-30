package com.assettracker.main.telegram_bot.database.dto;

import com.assettracker.main.telegram_bot.database.entity.BagEntity;
import com.assettracker.main.telegram_bot.database.entity.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private UUID id;
    private Long chatId;
    private String firstName;
    private String lastName;
    private String userName;
    private BagEntity bag;
    private UserStatus status;
}

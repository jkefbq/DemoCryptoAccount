package com.assettracker.main.telegram_bot.database.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Optional;

@Builder
@Data
public class UpdateDto {
    private Long chatId;
    private String firstName;
    private String lastName;
    private String userName;
    private Optional<String> userInput;
}

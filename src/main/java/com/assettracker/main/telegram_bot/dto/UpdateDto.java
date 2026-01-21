package com.assettracker.main.telegram_bot.dto;

import lombok.Data;

import java.util.Optional;

@Data
public class UpdateDto {
    private Long chatId;
    private String firstName;
    private String lastName;
    private String userName;
    private Optional<String> userInput;
}

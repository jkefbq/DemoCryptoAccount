package com.assettracker.main.telegram_bot.service;

import com.assettracker.main.telegram_bot.dto.UpdateDto;
import com.assettracker.main.telegram_bot.dto.UpdateMapper;
import com.assettracker.main.telegram_bot.UpdateMapperImpl;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Optional;

public class UpdateUtils {

    private static final UpdateMapper mapper = new UpdateMapperImpl();

    public static Long getChatId(Update update) {
        if (update.hasMessage()) {
            return update.getMessage().getChatId();
        } else if (update.hasCallbackQuery()) {
            return update.getCallbackQuery().getFrom().getId();
        } else {
            throw new IllegalStateException("update has no message or data");
        }
    }

    public static User getUser(Update update) {
        if (update.hasMessage()) {
            return update.getMessage().getFrom();
        } else if (update.hasCallbackQuery()) {
            return update.getCallbackQuery().getFrom();
        } else {
            throw new IllegalStateException("update has no message or data");
        }
    }

    public static UpdateDto toDto(Update update) {
        return mapper.toDto(update);
    }

    public static Optional<String> getUserInput(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            return Optional.of(update.getMessage().getText());
        } else {
            return Optional.empty();
        }
    }
}

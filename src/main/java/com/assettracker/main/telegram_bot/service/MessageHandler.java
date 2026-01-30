package com.assettracker.main.telegram_bot.service;

import com.assettracker.main.telegram_bot.database.dto.UpdateDto;
import com.assettracker.main.telegram_bot.events.Message;
import com.assettracker.main.telegram_bot.events.MessageEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.NoSuchElementException;

@Slf4j
@Component
@AllArgsConstructor
public class MessageHandler {

    private final ApplicationEventPublisher eventPublisher;

    public void handle(Update update) {
        String text = update.getMessage().getText().trim();
        UpdateDto dto = UpdateUtils.toDto(update);
        try {
            Message msg = Message.parseText(text);
            log.info("Command='{}' was successfully recognized, about to publishing event with message='{}'", text, msg);
            eventPublisher.publishEvent(new MessageEvent(this, msg, dto));
        } catch (NoSuchElementException e) {
            log.info("Command='{}' not recognized, about to publishing event with message=Message.UNKNOWN", text);
            eventPublisher.publishEvent(new MessageEvent(this, Message.UNKNOWN, dto));
        }
    }
}

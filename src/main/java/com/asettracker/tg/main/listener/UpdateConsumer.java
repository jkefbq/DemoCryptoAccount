package com.asettracker.tg.main.listener;

import com.asettracker.tg.main.service.GeneralButtonHandler;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@AllArgsConstructor
public class UpdateConsumer implements LongPollingSingleThreadUpdateConsumer {

    private final GeneralButtonHandler buttonHandler;
    private final MessageHandler messageHandler;

    @SneakyThrows
    @Override
    public void consume(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            messageHandler.handleAnyMessage(update);
        } else if (update.hasCallbackQuery()) {
            buttonHandler.handleAnyButton(update);
        }
    }
}

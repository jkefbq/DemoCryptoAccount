package com.asettracker.tg.myNew.listener;

import com.asettracker.tg.myNew.menu.MainMenu;
import com.asettracker.tg.myNew.service.GeneralButtonHandler;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class UpdateConsumer implements LongPollingSingleThreadUpdateConsumer {

    private final GeneralButtonHandler buttonHandler;
    private final MainMenu mainMenu;

    public UpdateConsumer(GeneralButtonHandler buttonHandler, MainMenu mainMenu) {
        this.buttonHandler = buttonHandler;
        this.mainMenu = mainMenu;
    }

    @SneakyThrows
    @Override
    public void consume(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            mainMenu.sendMainMenu(update);
        } else if (update.hasCallbackQuery()) {
            buttonHandler.handleAnyButton(update.getCallbackQuery());
        }
    }

}

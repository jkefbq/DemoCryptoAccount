package com.asettracker.tg.main.menu.main_menu;

import com.asettracker.tg.main.config.ChatId;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.concurrent.ThreadLocalRandom;

@Component
@AllArgsConstructor
public class RandomNumberButton implements IMainMenuButton {

    private final static String RANDOM_NUMBER_BUTTON = "randomNumber";
    private final TelegramClient telegramClient;

    @Override
    public InlineKeyboardButton getButton() {
        return InlineKeyboardButton.builder()
                .text("Случайное число")
                .callbackData(RANDOM_NUMBER_BUTTON)
                .build();
    }

    @Override
    public boolean canHandleButton(Update update) {
        return update.getCallbackQuery().getData().equals(RANDOM_NUMBER_BUTTON);
    }

    @SneakyThrows
    @Override
    public void handleButton(Update update) {
        SendMessage sendMessage = SendMessage.builder()
                .chatId(ChatId.get(update))
                .text("Ваше случайное число: " + ThreadLocalRandom.current().nextInt(100000))
                .build();
        telegramClient.execute(sendMessage);
    }
}
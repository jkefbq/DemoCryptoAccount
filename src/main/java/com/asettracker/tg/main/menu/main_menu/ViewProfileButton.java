package com.asettracker.tg.main.menu.main_menu;

import com.asettracker.tg.main.config.ChatId;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Component
@AllArgsConstructor
public class ViewProfileButton implements IMainMenuButton {

    private final static String VIEW_PROFILE_CALLBACK_DATA = "viewProfile";
    private final TelegramClient telegramClient;

    @Override
    public InlineKeyboardButton getButton() {
        return InlineKeyboardButton.builder()
                .text("Мой профиль")
                .callbackData(VIEW_PROFILE_CALLBACK_DATA)
                .build();
    }

    @Override
    public boolean canHandleButton(Update update) {
        return update.getCallbackQuery().getData().equals(VIEW_PROFILE_CALLBACK_DATA);
    }

    @SneakyThrows
    @Override
    public void handleButton(Update update) {
        SendMessage sendMessage = SendMessage.builder()
                .chatId(ChatId.get(update))
                .text("Вы нажали мой профиль")
                .build();
        telegramClient.execute(sendMessage);
    }
}

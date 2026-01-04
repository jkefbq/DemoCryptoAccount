package com.asettracker.tg.main.menu.main_menu;

import com.asettracker.tg.main.config.ChatId;
import com.asettracker.tg.main.menu.IMenu;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.List;

@Component
@AllArgsConstructor
public class MainMenu implements IMenu {

    private final TelegramClient telegramClient;
    private final List<IMainMenuButton> buttons;

    @SneakyThrows
    @Override
    public void sendMenu(Update update) {
        SendMessage sendMessage = SendMessage.builder()
                .chatId(ChatId.get(update))
                .text("С чего начнём?🧐")
                .replyMarkup(combineButtons(buttons))
                .build();
        telegramClient.execute(sendMessage);
    }
}
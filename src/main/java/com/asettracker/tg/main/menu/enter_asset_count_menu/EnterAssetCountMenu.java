package com.asettracker.tg.main.menu.enter_asset_count_menu;

import com.asettracker.tg.main.database.service.BagDbService;
import com.asettracker.tg.main.menu.IMenu;
import com.asettracker.tg.main.menu.asset_list_menu.UserChoose;
import com.asettracker.tg.main.service.ChatId;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;


@Component
@RequiredArgsConstructor
public class EnterAssetCountMenu implements IMenu {

    private final TelegramClient telegramClient;
    private final BagDbService bagDbService;
    private final UserChoose userChoose;

    @SneakyThrows
    @Override
    public void sendMenu(Update update) {
        SendMessage sendMessage = SendMessage.builder()
                .chatId(ChatId.get(update))
                .text("Введите количество монет:")
                .build();
        telegramClient.execute(sendMessage);
    }

    public void addAssetAndSendSuccess(Update update) {
        bagDbService.addAsset(userChoose, ChatId.get(update));
        sendSuccess(update);
    }

    @SneakyThrows
    public void sendSuccess(Update update) {
        SendMessage sendMessage = SendMessage.builder()
                .chatId(ChatId.get(update))
                .text("Успех! Монета уже в портфеле /bag")
                .build();
        telegramClient.execute(sendMessage);
    }
}

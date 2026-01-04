package com.asettracker.tg.main.menu.enter_asset_count_menu;

import com.asettracker.tg.main.config.ChatId;
import com.asettracker.tg.main.database.service.BagDbService;
import com.asettracker.tg.main.database.service.UserDbService;
import com.asettracker.tg.main.dto.UserStatus;
import com.asettracker.tg.main.menu.IMenu;
import com.asettracker.tg.main.menu.asset_list_menu.UserChoose;
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
    private final UserDbService userDbService;
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

    public void acceptCountAndCreateRecord(Update update) {
        addAssetToBag(update);
        sendSuccess(update);
    }

    public void addAssetToBag(Update update) {
        bagDbService.addAsset(userChoose.getCoinName(), userChoose.getCoinCount(), ChatId.get(update));
        bagDbService.actualizeBagAssets(ChatId.get(update));
        userDbService.findByChatId(ChatId.get(update)).ifPresent(u -> u.setUserStatus(UserStatus.FREE));
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

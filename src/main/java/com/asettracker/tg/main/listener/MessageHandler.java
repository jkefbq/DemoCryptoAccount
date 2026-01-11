package com.asettracker.tg.main.listener;

import com.asettracker.tg.main.database.entity.BagEntity;
import com.asettracker.tg.main.database.entity.UserEntity;
import com.asettracker.tg.main.database.service.BagDbService;
import com.asettracker.tg.main.database.service.UserDbService;
import com.asettracker.tg.main.database.UserStatus;
import com.asettracker.tg.main.menu.asset_list_menu.UserChoose;
import com.asettracker.tg.main.menu.bag_menu.BagMenu;
import com.asettracker.tg.main.menu.enter_asset_count_menu.EnterAssetCountMenu;
import com.asettracker.tg.main.menu.main_menu.MainMenu;
import com.asettracker.tg.main.service.ChatId;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Component
@AllArgsConstructor
public class MessageHandler {

    private final TelegramClient telegramClient;
    private final BagDbService bagDbService;
    private final UserDbService userDbService;
    private final MainMenu mainMenu;
    private final EnterAssetCountMenu enterAssetCountMenu;
    private final BagMenu bagMenu;
    private final UserChoose userChoose;

    public void handleAnyMessage(Update update) {
        var text = update.getMessage().getText();

        switch (text) {
            case "/start" -> handleStartMsg(update);
            case "/menu" -> handleMenu(update);
            case "/bag" -> bagMenu.sendMenu(update);
            default -> {
                if (isUserWaitingNumberStatus(update)) {
                    userChoose.setCoinCount(Double.parseDouble(text));
                    enterAssetCountMenu.addAssetAndSendSuccess(update);
                    userDbService.findByChatId(ChatId.get(update))
                            .ifPresent(user -> userDbService.setStatus(user, UserStatus.FREE));
                } else {
                    handleUnknown(update);
                }
            }
        }
    }

    private boolean isUserWaitingNumberStatus(Update update) {
        UserEntity user = userDbService
                .findByChatId(ChatId.get(update))
                .orElseThrow();
        return user.getUserStatus() == UserStatus.WAITING_FOR_NUMBER;
    }

    private void handleMenu(Update update) {
        mainMenu.sendMenu(update);
    }

    @SneakyThrows
    private void handleUnknown(Update update) {
        SendMessage sendMessage = SendMessage.builder()
                .text("Нераспознанная команда, хотите вернуться в меню? /menu")
                .chatId(ChatId.get(update))
                .build();
        telegramClient.execute(sendMessage);
    }

    @Transactional
    private void handleStartMsg(Update update) {
        mainMenu.sendMenu(update);
        new Thread(() -> createUserAndBag(update)).start();
    }

    @Transactional
    private void createUserAndBag(Update update) {
        if (!userDbService.hasUserByChatId(ChatId.get(update))) {
            userDbService.setUserBag(
                    new UserEntity(update),
                    bagDbService.createBag(new BagEntity(update))
            );
        }
    }
}

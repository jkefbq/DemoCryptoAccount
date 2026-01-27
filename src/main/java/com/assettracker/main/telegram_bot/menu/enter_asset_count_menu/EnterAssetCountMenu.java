package com.assettracker.main.telegram_bot.menu.enter_asset_count_menu;

import com.assettracker.main.telegram_bot.menu.IMenu;
import com.assettracker.main.telegram_bot.service.LastMessageService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.generics.TelegramClient;


@Component
@RequiredArgsConstructor
public class EnterAssetCountMenu implements IMenu {

    private final TelegramClient telegramClient;
    private final LastMessageService lastMessageService;

    @Override
    @SneakyThrows
    public void editMsgAndSendMenu(Long chatId, Integer messageId) {
        EditMessageText editMessageText = EditMessageText.builder()
                .chatId(chatId)
                .messageId(messageId)
                .text("Введите количество монет:")
                .build();
        telegramClient.execute(editMessageText);
    }

    @Override
    @SneakyThrows
    public void sendMenu(Long chatId) {
        SendMessage sendMessage = SendMessage.builder()
                .chatId(chatId)
                .text("Введите количество монет:")
                .build();
        Message msg = telegramClient.execute(sendMessage);
        lastMessageService.setLastMessage(chatId, msg.getMessageId());
    }

    @SneakyThrows
    public void sendSuccess(Long chatId) {
        SendMessage sendMessage = SendMessage.builder()
                .chatId(chatId)
                .text("Успех! Монета уже в портфеле")
                .build();
        Message msg = telegramClient.execute(sendMessage);
        lastMessageService.setLastMessage(chatId, msg.getMessageId());
    }
}

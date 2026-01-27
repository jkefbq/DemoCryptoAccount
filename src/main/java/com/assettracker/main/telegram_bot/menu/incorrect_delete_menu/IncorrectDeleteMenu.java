package com.assettracker.main.telegram_bot.menu.incorrect_delete_menu;

import com.assettracker.main.telegram_bot.menu.IMenu;
import com.assettracker.main.telegram_bot.menu.incorrect_update_asset_menu.CancelToMyAssets;
import com.assettracker.main.telegram_bot.service.LastMessageService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.List;

@Component
@AllArgsConstructor
public class IncorrectDeleteMenu implements IMenu {

    private final TelegramClient telegramClient;
    private final CreateAssetAfterTryDeleteButton createButton;
    private final CancelToMyAssets cancelButton;
    private final LastMessageService lastMessageService;

    @SneakyThrows
    @Override
    public void editMsgAndSendMenu(Long chatId, Integer messageId) {
        EditMessageText editMessageText = EditMessageText.builder()
                .chatId(chatId)
                .messageId(messageId)
                .text("В вашем портфеле нет такой монеты, хотите ее добавить?")
                .replyMarkup(combineButtons(List.of(createButton, cancelButton)))
                .build();
        telegramClient.execute(editMessageText);
    }

    @SneakyThrows
    @Override
    public void sendMenu(Long chatId) {
        SendMessage sendMessage = SendMessage.builder()
                .chatId(chatId)
                .text("В вашем портфеле нет такой монеты, хотите ее добавить?")
                .replyMarkup(combineButtons(List.of(createButton, cancelButton)))
                .build();
        Message msg = telegramClient.execute(sendMessage);
        lastMessageService.setLastMessage(chatId, msg.getMessageId());
    }

    @SneakyThrows
    public void EditMsgAndSendSuccess(Long chatId, Integer messageId) {
        EditMessageText editMessageText = EditMessageText.builder()
                .chatId(chatId)
                .messageId(messageId)
                .text("Успех! Монета удалена из вашего портфеля")
                .build();
        telegramClient.execute(editMessageText);
    }
}

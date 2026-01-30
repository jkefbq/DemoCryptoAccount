package com.assettracker.main.telegram_bot.menu.support_menu;

import com.assettracker.main.telegram_bot.menu.IMenu;
import com.assettracker.main.telegram_bot.menu.my_profile_menu.CancelToMainMenuButton;
import com.assettracker.main.telegram_bot.service.LastMessageService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.List;

@Component
@AllArgsConstructor
public class SupportMenu implements IMenu {

    private final CancelToMainMenuButton cancelButton;
    private final TelegramClient telegramClient;
    private final LastMessageService lastMessageService;

    @SneakyThrows
    @Override
    public void editMsgAndSendMenu(Long chatId, Integer messageId) {
        EditMessageText editMessageText = EditMessageText.builder()
                .chatId(chatId)
                .messageId(messageId)
                .text("Напишите ваш вопрос, а администратор постарается ответить вам как можно скорей")
                .replyMarkup(combineButtons(List.of(cancelButton)))
                .build();
        telegramClient.execute(editMessageText);
    }

    @SneakyThrows
    @Override
    public void sendMenu(Long chatId) {
        SendMessage sendMessage = SendMessage.builder()
                .chatId(chatId)
                .text("Напишите в чат ваш вопрос, а администратор постарается ответить вам как можно скорей")
                .replyMarkup(combineButtons(List.of(cancelButton)))
                .build();
        Message msg = telegramClient.execute(sendMessage);
        lastMessageService.setLastMessage(chatId, msg.getMessageId());
    }

    @SneakyThrows
    public void sendSuccess(Long chatId) {
        SendMessage sendMessage = SendMessage.builder()
                .chatId(chatId)
                .text("Успех! Администратор свяжется с вами как только сможет.")
                .replyMarkup(combineButtons(List.of(cancelButton)))
                .build();
        Message msg = telegramClient.execute(sendMessage);
        lastMessageService.setLastMessage(chatId, msg.getMessageId());
    }

    @SneakyThrows
    public void sendAnswer(Long chatId, String answer) {
        SendMessage sendMessage = SendMessage.builder()
                .chatId(chatId)
                .text("<b>Ответ от администратора:</b>\n<blockquote>" + answer + "</blockquote>")
                .parseMode(ParseMode.HTML)
                .build();
        Message msg = telegramClient.execute(sendMessage);
        lastMessageService.setLastMessage(chatId, msg.getMessageId());
    }
}

package com.assettracker.main.telegram_bot.menu.main_menu;

import com.assettracker.main.telegram_bot.menu.IMenu;
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
public class MainMenu implements IMenu {

    private final TelegramClient telegramClient;
    private final List<IMainMenuButton> buttons;
    private final LastMessageService lastMessageService;

    @SneakyThrows
    @Override
    public void editMsgAndSendMenu(Long chatId, Integer messageId) {
        EditMessageText editMessageText = EditMessageText.builder()
                .chatId(chatId)
                .messageId(messageId)
                .text(getText())
                .replyMarkup(combineButtons(buttons))
                .parseMode(ParseMode.HTML)
                .build();
        telegramClient.execute(editMessageText);
    }

    @SneakyThrows
    @Override
    public void sendMenu(Long chatId) {
        SendMessage sendMessage = SendMessage.builder()
                .chatId(chatId)
                .text(getText())
                .replyMarkup(combineButtons(buttons))
                .parseMode(ParseMode.HTML)
                .build();
        Message msg = telegramClient.execute(sendMessage);
        lastMessageService.setLastMessage(chatId, msg.getMessageId());
    }

    private String getText() {
        return """
                <code>          ГЛАВНОЕ МЕНЮ            \s
                --------------------------------- \s
                                    (_)           \s
                 _ .--..--.  ,--.   __   _ .--.   \s
                [ `.-. .-. |`'_\\ : [  | [ `.-. |  \s
                 | | | | | |// | |, | |  | | | |  \s
                [___||_||__]\\'-;__/[___][__| |__] \s
                </code>
                <blockquote>Твои активы под присмотром,
                что делаем?
                </blockquote>
                """;
    }
}
package com.assettracker.main.telegram_bot.menu.trade_with_ai_menu;

import com.assettracker.main.telegram_bot.menu.IMenu;
import com.assettracker.main.telegram_bot.menu.my_assets_menu.CancelToBagMenuButton;
import com.assettracker.main.telegram_bot.service.LastMessageService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.List;

@Component
@AllArgsConstructor
public class TradeWithAIMenu implements IMenu {

    private final TelegramClient telegramClient;
    private final LastMessageService lastMessageService;
    private final AIAdviceButton adviceButton;
    private final AIQuestionButton questionButton;
    private final CancelToBagMenuButton cancelButton;

    @SneakyThrows
    @Override
    public void editMsgAndSendMenu(Long chatId, Integer messageId) {
        EditMessageText editMessageText = EditMessageText.builder()
                .chatId(chatId)
                .messageId(messageId)
                .text(getMenuText())
                .replyMarkup(getMarkup())
                .parseMode(ParseMode.HTML)
                .build();
        telegramClient.execute(editMessageText);
    }

    private String getMenuText() {
        return """
                <code>          AI АССИСТЕНТ          \s
                ---------------------------------\s
                :::=======  ::: ===  :::====  :::
                ::: === === ::: ===  :::  === :::
                === === ===  =====   ======== ===
                ===     ===   ===    ===  === ===
                ===     ===   ===    ===  === ===
                </code><blockquote>
                AI ассистент поможет вам в вопросах
                связанных с торговлей опираясь на
                ваши активы и ситуацию на рынке.
                </blockquote>
                """;
    }

    @SneakyThrows
    @Override
    public void sendMenu(Long chatId) {
        SendMessage sendMessage = SendMessage.builder()
                .chatId(chatId)
                .text(getMenuText())
                .replyMarkup(getMarkup())
                .parseMode(ParseMode.HTML)
                .build();
        Message msg = telegramClient.execute(sendMessage);
        lastMessageService.setLastMessage(chatId, msg.getMessageId());
    }

    public InlineKeyboardMarkup getMarkup() {
        return new InlineKeyboardMarkup(
                List.of(
                        new InlineKeyboardRow(adviceButton.getButton()),
                        new InlineKeyboardRow(questionButton.getButton()),
                        new InlineKeyboardRow(cancelButton.getButton())
                )
        );
    }
}

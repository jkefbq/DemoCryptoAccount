package com.assettracker.main.telegram_bot.menu.bag_menu;

import com.assettracker.main.telegram_bot.database.service.BagService;
import com.assettracker.main.telegram_bot.menu.IMenu;
import com.assettracker.main.telegram_bot.menu.my_profile_menu.CancelToMainMenuButton;
import com.assettracker.main.telegram_bot.service.LastMessageService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BagMenu implements IMenu {

    private final TelegramClient telegramClient;
    private final AssetsButton assetsButton;
    private final CancelToMainMenuButton cancelButton;
    private final TradeWithAIButton tradeWithAIButton;
    private final BagService bagService;
    private final LastMessageService lastMessageService;

    public static String getMenuText() {
        return """
                <code>           МОЙ ПОРТФЕЛЬ         \s
                ---------------------------------\s
                :::::::.    :::.      .,-:::::/ \s
                 ;;;'';;'   ;;`;;   ,;;-'````'  \s
                 [[[__[[\\. ,[[ '[[, [[[   [[[[[[/
                 $$""\""Y$$c$$$cc$$$c"$$c.    "$$\s
                _88o,,od8P 888   888,`Y8bo,,,o88o
                ""YUMMMP"  YMM   ""`   `'YMUP"YMM
                </code><blockquote>
                Ты в своем портфеле, что делаем?
                </blockquote>""";
    }

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

    @SneakyThrows
    @Override
    public void sendMenu(Long chatId) {
        LocalDate createdAt = bagService.findByChatId(chatId).orElseThrow().getCreatedAt();
        SendMessage sendMessage = SendMessage.builder()
                .chatId(chatId)
                .text(String.format(getMenuText(), ChronoUnit.DAYS.between(createdAt, LocalDate.now())))
                .replyMarkup(getMarkup())
                .parseMode(ParseMode.HTML)
                .build();
        Message msg = telegramClient.execute(sendMessage);
        lastMessageService.setLastMessage(chatId, msg.getMessageId());
    }

    public InlineKeyboardMarkup getMarkup() {
        return new InlineKeyboardMarkup(
                List.of(
                        new InlineKeyboardRow(assetsButton.getButton()),
                        new InlineKeyboardRow(tradeWithAIButton.getButton()),
                        new InlineKeyboardRow(cancelButton.getButton())
                )
        );
    }
}

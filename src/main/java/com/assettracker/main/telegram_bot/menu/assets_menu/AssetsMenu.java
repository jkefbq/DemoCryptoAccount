package com.assettracker.main.telegram_bot.menu.assets_menu;

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
public class AssetsMenu implements IMenu {

    private final TelegramClient telegramClient;
    private final LastMessageService lastMessageService;
    private final MyAssetsButton myAssetsButton;
    private final AssetStatisticsButton statisticsButton;
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
                        new InlineKeyboardRow(myAssetsButton.getButton()),
                        new InlineKeyboardRow(statisticsButton.getButton()),
                        new InlineKeyboardRow(cancelButton.getButton())
                )
        );
    }

    public String getMenuText() {
        return """
                <code>              АКТИВЫ             \s
                ---------------------------------\s
                 .oooo.    .oooo.o  .oooo.o      \s
                `P  )88b  d88(  "8 d88(  "8      \s
                 .oP"888  `"Y88b.  `"Y88b.       \s
                d8(  888  o.  )88b o.  )88b      \s
                `Y888""8o 8""888P' 8""888P'      \s
                                 .o8             \s
                      .ooooo.  .o888oo   .oooo.o \s
                     d88' `88b   888    d88(  "8 \s
                     888ooo888   888    `"Y88b.  \s
                     888    .o   888 .  o.  )88b \s
                     `Y8bod8P'   "888"  8""888P' \s
                </code><blockquote>Твоя панель управления активами,
                какая стратегия торговли сегодня?
                </blockquote>
                """;
    }
}

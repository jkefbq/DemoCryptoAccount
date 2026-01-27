package com.assettracker.main.telegram_bot.menu.ai_advice_menu;

import com.assettracker.main.telegram_bot.database.service.BagService;
import com.assettracker.main.telegram_bot.menu.IMenu;
import com.assettracker.main.telegram_bot.menu.my_assets_menu.CancelToBagMenuButton;
import com.assettracker.main.telegram_bot.service.AIService;
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
public class AIAdviceMenu implements IMenu {

    private final LastMessageService lastMessageService;
    private final TelegramClient telegramClient;
    private final AIService aiService;
    private final BagService bagService;
    private final CancelToBagMenuButton cancelButton;

    @SneakyThrows
    @Override
    public void editMsgAndSendMenu(Long chatId, Integer messageId) {
        var assets = bagService.findByChatId(chatId).orElseThrow().getAssets();
        String advice = aiService.getAdvice(assets);
        EditMessageText editMessageText = EditMessageText.builder()
                .text(getMenuText(advice))
                .messageId(messageId)
                .replyMarkup(combineButtons(List.of(cancelButton)))
                .parseMode(ParseMode.HTML)
                .build();
        telegramClient.execute(editMessageText);
    }

    private String getMenuText(String aiAnswer) {
        return "" + aiAnswer;
    }

    @SneakyThrows
    @Override
    public void sendMenu(Long chatId) {
        var assets = bagService.findByChatId(chatId).orElseThrow().getAssets();
        String advice = aiService.getAdvice(assets);
        SendMessage sendMessage = SendMessage.builder()
                .text(getMenuText(advice))
                .replyMarkup(combineButtons(List.of(cancelButton)))
                .parseMode(ParseMode.HTML)
                .build();
        Message msg = telegramClient.execute(sendMessage);
        lastMessageService.setLastMessage(chatId, msg.getMessageId());
    }
}

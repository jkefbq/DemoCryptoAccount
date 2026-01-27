package com.assettracker.main.telegram_bot.menu.my_assets_menu;

import com.assettracker.main.telegram_bot.database.service.BagService;
import com.assettracker.main.telegram_bot.menu.IMenu;
import com.assettracker.main.telegram_bot.menu.asset_list_menu.Coins;
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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Component
@AllArgsConstructor
public class MyAssetsMenu implements IMenu {

    private final TelegramClient telegramClient;
    private final CreateAssetButton createAssetButton;
    private final UpdateAssetButton updateAssetButton;
    private final DeleteAssetButon deleteAssetButon;
    private final CancelToAssetsMenuButton cancelButton;
    private final LastMessageService lastMessageService;
    private final BagService bagService;

    @SneakyThrows
    @Override
    public void editMsgAndSendMenu(Long chatId, Integer messageId) {
        Map<Coins, BigDecimal> coinAndCount =  bagService.findByChatId(chatId).orElseThrow().getAssets();
        Map<Coins, Map.Entry<BigDecimal, BigDecimal>> coinCountPrice = bagService.getCoinCountAndPrices(coinAndCount);
        EditMessageText editMessageText = EditMessageText.builder()
                .chatId(chatId)
                .messageId(messageId)
                .text(genAssetList(coinCountPrice))
                .replyMarkup(getMarkup())
                .parseMode(ParseMode.HTML)
                .build();
        telegramClient.execute(editMessageText);
    }

    @SneakyThrows
    @Override
    public void sendMenu(Long chatId) {
        Map<Coins, BigDecimal> coinAndCount =  bagService.findByChatId(chatId).orElseThrow().getAssets();
        Map<Coins, Map.Entry<BigDecimal, BigDecimal>> coinCountPrice = bagService.getCoinCountAndPrices(coinAndCount);
        SendMessage sendMessage = SendMessage.builder()
                .chatId(chatId)
                .text(genAssetList(coinCountPrice))
                .replyMarkup(getMarkup())
                .parseMode(ParseMode.HTML)
                .build();
        Message msg = telegramClient.execute(sendMessage);
        lastMessageService.setLastMessage(chatId, msg.getMessageId());
    }

    private String genAssetList(Map<Coins, Map.Entry<BigDecimal, BigDecimal>> coinAndCount) {
        StringBuilder result = new StringBuilder("<b>Ваши активы:</b><pre><code>\nМОНЕТА      " +
                "КОЛИЧЕСТВО      ЦЕНА\n---------------------------------------\n");
        AtomicReference<BigDecimal> totalCost = new AtomicReference<>(BigDecimal.ZERO);
        coinAndCount.forEach((coin, entry) -> {
            result.append(String.format("%-10s : %-13s %s%n", coin.name(),
                    entry.getKey().setScale(5, RoundingMode.HALF_UP),
                    entry.getValue().multiply(entry.getKey())
                            .setScale(2, RoundingMode.HALF_UP) + "$"));
            totalCost.updateAndGet(curr -> curr.add(entry.getKey().multiply(entry.getValue())));
        });
        result.append("---------------------------------------\n\n")
                .append("💱 КОЛ-ВО  : ").append(coinAndCount.size()).append("шт\n")
                .append("💱 СУММА   : ").append(totalCost.get()
                        .setScale(2, RoundingMode.HALF_UP)).append("$\n");
        return result.append("</code></pre>").toString();
    }

    public InlineKeyboardMarkup getMarkup() {
        return new InlineKeyboardMarkup(
                List.of(
                        new InlineKeyboardRow(createAssetButton.getButton()),
                        new InlineKeyboardRow(updateAssetButton.getButton()),
                        new InlineKeyboardRow(deleteAssetButon.getButton()),
                        new InlineKeyboardRow(cancelButton.getButton())
                )
        );
    }
}

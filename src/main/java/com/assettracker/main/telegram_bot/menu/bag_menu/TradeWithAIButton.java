package com.assettracker.main.telegram_bot.menu.bag_menu;

import com.assettracker.main.telegram_bot.menu.Buttons;
import lombok.Getter;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

@Component
public class TradeWithAIButton implements IBagMenuButton {

    @Getter
    private final String callbackData = Buttons.TRADE_WITH_AI.getCallbackData();

    @Override
    public InlineKeyboardButton getButton() {
        return InlineKeyboardButton.builder()
                .text("🧠 Торговля с AI")
                .callbackData(callbackData)
                .build();
    }
}

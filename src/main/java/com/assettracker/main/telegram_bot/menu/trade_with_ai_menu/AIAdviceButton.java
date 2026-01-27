package com.assettracker.main.telegram_bot.menu.trade_with_ai_menu;

import com.assettracker.main.telegram_bot.menu.Buttons;
import lombok.Getter;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

@Component
public class AIAdviceButton implements ITradeWithAiMenuButton {

    @Getter
    private final String callbackData = Buttons.AI_ADVICE.getCallbackData();

    @Override
    public InlineKeyboardButton getButton() {
        return InlineKeyboardButton.builder()
                .text("💹 Получить совет")
                .callbackData(callbackData)
                .build();
    }
}

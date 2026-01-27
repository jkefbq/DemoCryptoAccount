package com.assettracker.main.telegram_bot.menu.bag_menu;

import com.assettracker.main.telegram_bot.menu.Buttons;
import lombok.Getter;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

@Component
public class AssetsButton implements IBagMenuButton {

    @Getter
    private final String callbackData = Buttons.ASSETS.getCallbackData();

    @Override
    public InlineKeyboardButton getButton() {
        return InlineKeyboardButton.builder()
                .text("💹 Активы")
                .callbackData(callbackData)
                .build();
    }
}

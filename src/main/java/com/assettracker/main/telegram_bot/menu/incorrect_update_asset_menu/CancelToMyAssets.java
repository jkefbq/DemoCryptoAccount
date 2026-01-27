package com.assettracker.main.telegram_bot.menu.incorrect_update_asset_menu;

import com.assettracker.main.telegram_bot.menu.Buttons;
import lombok.Getter;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

@Component
public class CancelToMyAssets implements IIncorrectUpdateAssetMenuButton {

    @Getter
    private final String callbackData =
            Buttons.CANCEL_TO_MY_ASSETS.getCallbackData();

    @Override
    public InlineKeyboardButton getButton() {
        return InlineKeyboardButton.builder()
                .text("Назад")
                .callbackData(callbackData)
                .build();
    }

}
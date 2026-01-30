package com.assettracker.main.telegram_bot.menu.incorrect_update_asset_menu;

import com.assettracker.main.telegram_bot.events.Button;
import lombok.Getter;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

@Component
public class ForceCreateAssetButton implements IIncorrectUpdateAssetMenuButton {

    @Getter
    private final String callbackData = Button.FORCE_CREATE_ASSET.getCallbackData();

    @Override
    public InlineKeyboardButton getButton() {
        return InlineKeyboardButton.builder()
                .text("Добавить")
                .callbackData(callbackData)
                .build();
    }
}

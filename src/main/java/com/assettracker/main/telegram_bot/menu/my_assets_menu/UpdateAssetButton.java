package com.assettracker.main.telegram_bot.menu.my_assets_menu;

import com.assettracker.main.telegram_bot.menu.Buttons;
import lombok.Getter;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

@Component
public class UpdateAssetButton implements IMyAssetsMenuButton {

    @Getter
    private final String callbackData = Buttons.UPDATE_ASSET.getCallbackData();

    @Override
    public InlineKeyboardButton getButton() {
        return InlineKeyboardButton.builder()
                .text("🔄 Обновить монету")
                .callbackData(callbackData)
                .build();
    }
}

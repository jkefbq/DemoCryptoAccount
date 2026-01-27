package com.assettracker.main.telegram_bot.menu.my_assets_menu;

import com.assettracker.main.telegram_bot.menu.Buttons;
import lombok.Getter;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

@Component
public class DeleteAssetButon implements IMyAssetsMenuButton {

    @Getter
    public final String DELETE_ASSET_BUTTON_CALLBACK_DATA = Buttons.DELETE_ASSET.getCallbackData();


    @Override
    public InlineKeyboardButton getButton() {
        return InlineKeyboardButton.builder()
                .text("❌ Удалить монету")
                .callbackData(DELETE_ASSET_BUTTON_CALLBACK_DATA)
                .build();
    }

    @Override
    public String getCallbackData() {
        return DELETE_ASSET_BUTTON_CALLBACK_DATA;
    }
}

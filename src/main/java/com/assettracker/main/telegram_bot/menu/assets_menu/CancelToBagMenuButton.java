package com.assettracker.main.telegram_bot.menu.my_assets_menu;

import com.assettracker.main.telegram_bot.menu.Buttons;
import com.assettracker.main.telegram_bot.menu.assets_menu.IAssetsMenuButton;
import lombok.Getter;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

@Component
public class CancelToBagMenuButton implements IAssetsMenuButton {

    @Getter
    private final String callbackData = Buttons.CANCEL_TO_BAG_MENU.getCallbackData();


    @Override
    public InlineKeyboardButton getButton() {
        return InlineKeyboardButton.builder()
                .text("Назад")
                .callbackData(callbackData)
                .build();
    }
}

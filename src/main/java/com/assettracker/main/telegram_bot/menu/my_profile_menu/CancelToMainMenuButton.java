package com.assettracker.main.telegram_bot.menu.my_profile_menu;

import com.assettracker.main.telegram_bot.menu.Buttons;
import lombok.Getter;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

@Component
public class CancelToMainMenuButton implements IMyProfileMenuButton {

    @Getter
    private final String callbackData = Buttons.CANCEL_TO_MENU.getCallbackData();

    @Override
    public InlineKeyboardButton getButton() {
        return InlineKeyboardButton.builder()
                .text("Назад")
                .callbackData(callbackData)
                .build();
    }
}

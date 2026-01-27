package com.assettracker.main.telegram_bot.menu.incorrect_delete_menu;

import com.assettracker.main.telegram_bot.menu.Buttons;
import lombok.Getter;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

@Component
public class CreateAssetAfterTryDeleteButton implements IIncorrectDeleteMenuButton {

    @Getter
    private final String CREATE_ASSET_AFTER_TRY_DELETE_CALLBACK_DATA =
            Buttons.CREATE_ASSET_AFTER_TRY_DELETE.getCallbackData();

    @Override
    public InlineKeyboardButton getButton() {
        return InlineKeyboardButton.builder()
                .text("Добавить монету")
                .callbackData(CREATE_ASSET_AFTER_TRY_DELETE_CALLBACK_DATA)
                .build();
    }

    @Override
    public String getCallbackData() {
        return CREATE_ASSET_AFTER_TRY_DELETE_CALLBACK_DATA;
    }
}

package com.asettracker.tg.main.menu.asset_list_menu;

import com.asettracker.tg.main.menu.enter_asset_count_menu.EnterAssetCountMenu;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

@Component
@AllArgsConstructor
public class TetherButton implements IAssetListMenuButton {

    private static final String TETHER_BUTTON_CALLBACK_DATA = "tetherButton";
    private final EnterAssetCountMenu enterAssetCountMenu;
    private final UserChoose userChoose;

    @Override
    public InlineKeyboardButton getButton() {
        return InlineKeyboardButton.builder()
                .text("USDT")
                .callbackData(TETHER_BUTTON_CALLBACK_DATA)
                .build();
    }

    @Override
    public boolean canHandleButton(Update update) {
        return update.getCallbackQuery().getData().equals(TETHER_BUTTON_CALLBACK_DATA);
    }

    @Override
    public void handleButton(Update update) {
        userChoose.setCoinName("tether");
        enterAssetCountMenu.sendMenu(update);
    }
}
package com.asettracker.tg.main.menu.asset_list_menu;

import com.asettracker.tg.main.menu.enter_asset_count_menu.EnterAssetCountMenu;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

@Component
@AllArgsConstructor
public class LisaButton implements IAssetListMenuButton {

    private static final String LISA_BUTTON_CALLBACK_DATA = "lisaButton";
    private final EnterAssetCountMenu enterAssetCountMenu;
    private final UserChoose userChoose;

    @Override
    public InlineKeyboardButton getButton() {
        return InlineKeyboardButton.builder()
                .text("LISA")
                .callbackData(LISA_BUTTON_CALLBACK_DATA)
                .build();
    }

    @Override
    public boolean canHandleButton(Update update) {
        return update.getCallbackQuery().getData().equals(LISA_BUTTON_CALLBACK_DATA);
    }

    @Override
    public void handleButton(Update update) {
        userChoose.setCoinName("lisa");
        enterAssetCountMenu.sendMenu(update);
    }
}
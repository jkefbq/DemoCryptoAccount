package com.asettracker.tg.main.menu.bag_menu;

import com.asettracker.tg.main.menu.asset_list_menu.AssetListMenu;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;


@Component
@AllArgsConstructor
public class AddAssetButton implements IBagMenuButton {

    private static final String ADD_ASSET_BUTTON_CALLBACK_DATA = "addAsset";
    private final AssetListMenu assetListMenu;

    @Override
    public InlineKeyboardButton getButton() {
        return InlineKeyboardButton.builder()
                .text("Добавить монету")
                .callbackData(ADD_ASSET_BUTTON_CALLBACK_DATA)
                .build();
    }

    @Override
    public boolean canHandleButton(Update update) {
        return update.getCallbackQuery().getData().equals(ADD_ASSET_BUTTON_CALLBACK_DATA);
    }

    @Override
    public void handleButton(Update update) {
        assetListMenu.sendMenu(update);
    }
}

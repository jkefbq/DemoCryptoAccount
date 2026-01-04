package com.asettracker.tg.main.menu.main_menu;

import com.asettracker.tg.main.menu.bag_menu.BagMenu;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

@Component
@AllArgsConstructor
public class ViewBagButton implements IMainMenuButton {

    private static final String VIEW_BAG_CALLBACK_DATA = "viewBag";
    private final BagMenu bagMenu;

    @Override
    public InlineKeyboardButton getButton() {
        return InlineKeyboardButton.builder()
                .text("Мой портфель")
                .callbackData(VIEW_BAG_CALLBACK_DATA)
                .build();
    }

    @Override
    public boolean canHandleButton(Update update) {
        return update.getCallbackQuery().getData().equals(VIEW_BAG_CALLBACK_DATA);
    }

    @SneakyThrows
    @Override
    public void handleButton(Update update) {
        bagMenu.sendMenu(update);
    }
}

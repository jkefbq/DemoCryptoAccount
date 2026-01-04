package com.asettracker.tg.main.menu;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public interface IMenu {

    void sendMenu(Update update);

    default InlineKeyboardMarkup combineButtons(List<? extends IButton> buttons) {
        List<InlineKeyboardRow> rows = new ArrayList<>();
        buttons.forEach(button -> {
            rows.add(new InlineKeyboardRow(button.getButton()));
        });
        return new InlineKeyboardMarkup(rows);
    }
}

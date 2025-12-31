package com.asettracker.tg.myNew.menu;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.List;
import java.util.Map;

public interface MenuProvider {
    List<InlineKeyboardRow> getMenuButtonsInRows(Map<String, InlineKeyboardButton> buttons);
}

package com.asettracker.tg.myNew.menu;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

public interface ButtonHandler {
    boolean canHandleButton(CallbackQuery callbackButtonQuery);
    void handleButton(CallbackQuery callbackQuery);
}

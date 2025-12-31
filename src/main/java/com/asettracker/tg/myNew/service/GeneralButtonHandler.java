package com.asettracker.tg.myNew.service;

import com.asettracker.tg.myNew.menu.ButtonHandler;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.List;

@Service
@AllArgsConstructor
public class GeneralButtonHandler {
    private List<ButtonHandler> allHandlers;

    public void handleAnyButton(CallbackQuery callbackButtonQuery) {
        allHandlers.forEach(handler -> {
            if (handler.canHandleButton(callbackButtonQuery)) {
                handler.handleButton(callbackButtonQuery);
            }
        });
    }
}

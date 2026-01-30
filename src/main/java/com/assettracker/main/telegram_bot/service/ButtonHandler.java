package com.assettracker.main.telegram_bot.service;

import com.assettracker.main.telegram_bot.events.AssetButtonEvent;
import com.assettracker.main.telegram_bot.events.Button;
import com.assettracker.main.telegram_bot.events.ButtonEvent;
import com.assettracker.main.telegram_bot.menu.IButton;
import com.assettracker.main.telegram_bot.menu.asset_list_menu.Coins;
import com.assettracker.main.telegram_bot.menu.asset_list_menu.IAsset;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class ButtonHandler {

    private final List<? extends IButton> buttons;
    private final ApplicationEventPublisher eventPublisher;

    public void handle(CallbackQuery callbackQuery) {
        var chatId = callbackQuery.getFrom().getId();

        buttons.forEach(button -> {
            if (button.getCallbackData().equals(callbackQuery.getData())) {
                if (button instanceof IAsset) {
                    Coins thisCoin = ((IAsset) button).getCoin();
                    log.info("about to publishing event with Coins={} and chatId={}", thisCoin, chatId);
                    eventPublisher.publishEvent(new AssetButtonEvent(this, thisCoin, chatId));
                } else {
                    Button thisButton = Button.parseCallbackData(callbackQuery.getData());
                    log.info("about to publishing event with Button={} and chatId={}", thisButton, chatId);
                    eventPublisher.publishEvent(new ButtonEvent(this, thisButton, chatId));
                }
            }
        });
    }
}

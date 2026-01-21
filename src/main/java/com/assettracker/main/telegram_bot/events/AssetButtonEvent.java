package com.assettracker.main.telegram_bot.events;

import com.assettracker.main.telegram_bot.buttons.menu.asset_list_menu.Coins;
import lombok.Getter;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

@ToString
public class AssetButtonEvent extends ApplicationEvent {

    @Getter
    private final Coins coin;
    @Getter
    private final Long chatId;

    public AssetButtonEvent(Object source, Coins coin, Long chatId) {
        super(source);
        this.coin = coin;
        this.chatId = chatId;
    }
}

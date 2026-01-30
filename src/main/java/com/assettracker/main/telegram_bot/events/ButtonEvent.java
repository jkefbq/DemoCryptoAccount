package com.assettracker.main.telegram_bot.events;

import lombok.Getter;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

@ToString
public class ButtonEvent extends ApplicationEvent {

    @Getter
    private final Button button;
    @Getter
    private final Long chatId;

    public ButtonEvent(Object source, Button button, Long chatId) {
        super(source);
        this.button = button;
        this.chatId = chatId;
    }
}

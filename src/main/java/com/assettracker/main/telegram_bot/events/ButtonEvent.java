package com.assettracker.main.telegram_bot.events;

import lombok.Getter;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

@ToString
public class ButtonEvent extends ApplicationEvent {

    @Getter
    private final Buttons button;
    @Getter
    private final Long chatId;

    public ButtonEvent(Object source, Buttons button, Long chatId) {
        super(source);
        this.button = button;
        this.chatId = chatId;
    }
}

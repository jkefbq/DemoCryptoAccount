package com.assettracker.main.telegram_bot.events;

import com.assettracker.main.telegram_bot.dto.UpdateDto;
import lombok.Getter;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

@ToString
public class MessageEvent extends ApplicationEvent {

    @Getter
    private final Messages message;
    @Getter
    private final UpdateDto updateDto;

    public MessageEvent(Object source, Messages message, UpdateDto updateDto) {
        super(source);
        this.message = message;
        this.updateDto = updateDto;
    }
}

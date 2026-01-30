package com.assettracker.main.telegram_bot.events;

import lombok.Getter;

import java.util.Arrays;

public enum Message {
    UNKNOWN(""),
    START("/start"),
    MENU("/menu"),
    PROFILE("/profile"),
    BAG("/bag");

    @Getter
    private final String text;

    Message(String text) {
        this.text = text;
    }

    public static Message parseText(String text) {
        return Arrays.stream(Message.values())
                .filter(msg -> msg.getText().equals(text))
                .findFirst()
                .orElseThrow();
    }
}

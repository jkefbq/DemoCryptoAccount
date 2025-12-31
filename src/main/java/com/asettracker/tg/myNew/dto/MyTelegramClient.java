package com.asettracker.tg.myNew.dto;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Component
public class MyTelegramClient {
    @Getter
    private final TelegramClient telegramClient;

    public MyTelegramClient(@Value("${TG_KEY}") String tgToken) {
        this.telegramClient = new OkHttpTelegramClient(tgToken);
    }
}

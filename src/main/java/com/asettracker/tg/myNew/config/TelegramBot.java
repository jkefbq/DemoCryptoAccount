package com.asettracker.tg.myNew.config;

import com.asettracker.tg.myNew.listener.UpdateConsumer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;

@Component
public class TelegramBot implements SpringLongPollingBot {

    private final String TG_TOKEN;
    private final UpdateConsumer updateConsumer;

    public TelegramBot(@Value("${TG_KEY}") String tgToken, UpdateConsumer updateConsumer) {
        this.updateConsumer = updateConsumer;
        this.TG_TOKEN = tgToken;
    }

    @Override
    public String getBotToken() {
        return TG_TOKEN;
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return updateConsumer;
    }
}

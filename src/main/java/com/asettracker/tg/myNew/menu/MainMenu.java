package com.asettracker.tg.myNew.menu;

import com.asettracker.tg.myNew.dto.MyTelegramClient;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class MainMenu implements MenuProvider, ButtonHandler {

    private final static String VIEW_BAG_BUTTON = "viewBag";
    private final static String VIEW_PROFILE_BUTTON = "viewProfile";
    private final static String RANDOM_NUMBER_BUTTON = "randomNumber";
    private final TelegramClient telegramClient;

    public MainMenu(MyTelegramClient myTelegramClient) {
        this.telegramClient = myTelegramClient.getTelegramClient();
    }

    public Map<String, InlineKeyboardButton> getMenuButtons() {
        return Map.of(
                VIEW_BAG_BUTTON,
                InlineKeyboardButton.builder()
                        .text("my bag")
                        .callbackData(VIEW_BAG_BUTTON)
                        .build(),
                VIEW_PROFILE_BUTTON,
                InlineKeyboardButton.builder()
                        .text("my profile")
                        .callbackData(VIEW_PROFILE_BUTTON)
                        .build(),
                RANDOM_NUMBER_BUTTON,
                InlineKeyboardButton.builder()
                        .text("random number")
                        .callbackData(RANDOM_NUMBER_BUTTON)
                        .build()
        );
    }

    @Override
    public List<InlineKeyboardRow> getMenuButtonsInRows(
            Map<String, InlineKeyboardButton> buttons
    ) {
        return List.of(
                new InlineKeyboardRow(buttons.get(VIEW_BAG_BUTTON)),
                new InlineKeyboardRow(buttons.get(VIEW_PROFILE_BUTTON)),
                new InlineKeyboardRow(buttons.get(RANDOM_NUMBER_BUTTON))
        );
    }

    @Override
    public boolean canHandleButton(CallbackQuery callbackButtonQuery) {
        return getMenuButtons().containsKey(callbackButtonQuery.getData());
    }

    @Override
    public void handleButton(CallbackQuery callbackQuery) {
        switch (callbackQuery.getData()) {
            case VIEW_BAG_BUTTON -> handleViewBagButton(callbackQuery);
            case VIEW_PROFILE_BUTTON -> handleViewProfileButton(callbackQuery);
            case RANDOM_NUMBER_BUTTON -> handleRandomNumberButton(callbackQuery);
            default -> throw new IllegalArgumentException(
                    "class '" + getClass() + "' can't handle this button");
        }
    }

    @SneakyThrows
    private void handleRandomNumberButton(CallbackQuery callbackQuery) {
        SendMessage sendMessage = SendMessage.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .text("ваше рандомное число: " + ThreadLocalRandom.current().nextInt(100000))
                .build();
        telegramClient.execute(sendMessage);
    }

    @SneakyThrows
    private void handleViewProfileButton(CallbackQuery callbackQuery) {
        SendMessage sendMessage = SendMessage.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .text("вы нажали мой профиль")
                .build();
        telegramClient.execute(sendMessage);
    }

    @SneakyThrows
    private void handleViewBagButton(CallbackQuery callbackQuery) {
        SendMessage sendMessage = SendMessage.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .text("вы нажали мой портфель")
                .build();
        telegramClient.execute(sendMessage);
    }

    @SneakyThrows
    public void sendMainMenu(Update update) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup(
                getMenuButtonsInRows(getMenuButtons())
        );
        SendMessage sendMessage = SendMessage.builder()
                .chatId(update.getMessage().getChatId())
                .text("С чего начнем?")
                .replyMarkup(markup)
                .build();
        telegramClient.execute(sendMessage);
    }
}

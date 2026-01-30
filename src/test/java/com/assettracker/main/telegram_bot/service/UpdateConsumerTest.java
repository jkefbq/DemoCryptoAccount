package com.assettracker.main.telegram_bot.service;

import com.assettracker.main.telegram_bot.events.Button;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UpdateConsumerTest {

    @Mock
    MessageHandler messageHandler;
    @Mock
    ButtonHandler buttonHandler;
    @InjectMocks
    private UpdateConsumer updateConsumer;

    public Update getTooledUpdate_withMessage() {
        Update update = new Update();
        Message message = new Message();
        update.setMessage(message);
        User user = new User(1L, "firstname", false);
        user.setLastName("lastname");
        user.setUserName("username");
        update.getMessage().setFrom(user);
        return update;
    }

    public Update getTooledUpdate_withCallbackQuery() {
        Update update = new Update();
        User user = new User(1L, "firstname", false);
        user.setLastName("lastname");
        user.setUserName("username");
        CallbackQuery callbackQuery = new CallbackQuery();
        callbackQuery.setFrom(user);
        update.setCallbackQuery(callbackQuery);
        return update;
    }

    @Test
    public void consumeTest_withMessage() {
        var update = getTooledUpdate_withMessage();
        update.getMessage().setText(com.assettracker.main.telegram_bot.events.Message.BAG.getText());
        doNothing().when(messageHandler).handle(any());

        updateConsumer.consume(update);

        await().atMost(2, TimeUnit.SECONDS).untilAsserted(() ->
                verify(messageHandler, times(1)).handle(any()));
    }

    @Test
    public void consumeTest_withCallbackQuery() {
        var update = getTooledUpdate_withCallbackQuery();
        update.getCallbackQuery().setData(Button.MY_BAG.getCallbackData());
        doNothing().when(buttonHandler).handle(any());

        updateConsumer.consume(update);

        await().atMost(3, TimeUnit.SECONDS).untilAsserted(() ->
                verify(buttonHandler, times(1)).handle(any()));
    }
}

package com.assettracker.main.telegram_bot.service;

import com.assettracker.main.telegram_bot.events.MessageEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.chat.Chat;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MessageHandlerTest {

    private final MessageHandler messageHandler;
    private final ApplicationEventPublisher eventPublisher;
    @Captor
    private ArgumentCaptor<MessageEvent> argumentCaptor;

    public MessageHandlerTest() {
        ApplicationEventPublisher eventPublisher = Mockito.mock();
        doNothing().when(eventPublisher).publishEvent(any());
        this.eventPublisher = eventPublisher;
        this.messageHandler = new MessageHandler(eventPublisher);
    }
    
    public Update getTooledUpdate_withMessage() {
        Update update = new Update();
        Message message = new Message();
        update.setMessage(message);
        User user = new User(1L, "firstname", false);
        user.setLastName("lastname");
        user.setUserName("username");
        update.getMessage().setFrom(user);
        Chat chat = Mockito.mock();
        when(chat.getId()).thenReturn(1L);
        update.getMessage().setChat(chat);
        return update;
    }

    @Test
    public void publishMessageEventTest_StartMessage() {
        Update update = getTooledUpdate_withMessage();
        update.getMessage().setText(com.assettracker.main.telegram_bot.events.Message.START.getText());
        messageHandler.handle(update);

        verify(eventPublisher, times(1)).publishEvent(argumentCaptor.capture());
        var event = argumentCaptor.getValue();
        assertEquals(event.getMessage(), com.assettracker.main.telegram_bot.events.Message.START);
        assertEquals(event.getUpdateDto().getFirstName(), update.getMessage().getFrom().getFirstName());
        assertEquals(event.getUpdateDto().getUserInput().orElseThrow(), update.getMessage().getText());
    }

    @Test
    public void publishMessageEventTest_UnknownMessage() {
        Update update = getTooledUpdate_withMessage();
        update.getMessage().setText("" + ThreadLocalRandom.current().nextInt());
        messageHandler.handle(update);

        verify(eventPublisher, times(1)).publishEvent(argumentCaptor.capture());
        var event = argumentCaptor.getValue();
        assertEquals(event.getMessage(), com.assettracker.main.telegram_bot.events.Message.UNKNOWN);
        assertEquals(event.getUpdateDto().getFirstName(), update.getMessage().getFrom().getFirstName());
        assertEquals(event.getUpdateDto().getUserInput().orElseThrow(), update.getMessage().getText());
    }
}

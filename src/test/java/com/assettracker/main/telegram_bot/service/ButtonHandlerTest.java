package com.assettracker.main.telegram_bot.service;

import com.assettracker.main.telegram_bot.events.AssetButtonEvent;
import com.assettracker.main.telegram_bot.events.Button;
import com.assettracker.main.telegram_bot.events.ButtonEvent;
import com.assettracker.main.telegram_bot.menu.asset_list_menu.BitcoinButton;
import com.assettracker.main.telegram_bot.menu.asset_list_menu.Coins;
import com.assettracker.main.telegram_bot.menu.main_menu.ViewBagButton;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ButtonHandlerTest {

    private final ApplicationEventPublisher eventPublisher;
    private final ButtonHandler buttonHandler;
    @Captor
    private ArgumentCaptor<ApplicationEvent> argumentCaptor;

    public ButtonHandlerTest() {
        ApplicationEventPublisher eventPublisher = Mockito.mock();
        doNothing().when(eventPublisher).publishEvent(any());
        this.eventPublisher = eventPublisher;
        this.buttonHandler = new ButtonHandler(List.of(new ViewBagButton(), new BitcoinButton()), eventPublisher);
    }

    public CallbackQuery getTooledCallbackQuery() {
        CallbackQuery callbackQuery = new CallbackQuery();
        callbackQuery.setFrom(new User(1L, "username", false));
        return callbackQuery;
    }

    @Test
    public void publishRegularButtonEvent_ViewBagButton() {
        CallbackQuery callbackQuery = getTooledCallbackQuery();
        callbackQuery.setData(Button.MY_BAG.getCallbackData());

        buttonHandler.handle(callbackQuery);

        verify(eventPublisher, times(1)).publishEvent(argumentCaptor.capture());
        assertTrue(argumentCaptor.getValue() instanceof ButtonEvent);
        var event = (ButtonEvent) argumentCaptor.getValue();
        assertEquals(Button.MY_BAG, event.getButton());
        assertEquals(callbackQuery.getData(), event.getButton().getCallbackData());
        assertEquals(event.getChatId(), callbackQuery.getFrom().getId());
    }

    @Test
    public void publishAssetButtonEvent_BitcoinButton() {
        CallbackQuery callbackQuery = getTooledCallbackQuery();
        callbackQuery.setData(Coins.BITCOIN.getIdsName());

        buttonHandler.handle(callbackQuery);

        verify(eventPublisher, times(1)).publishEvent(argumentCaptor.capture());
        assertTrue(argumentCaptor.getValue() instanceof AssetButtonEvent);
        var event = (AssetButtonEvent) argumentCaptor.getValue();
        assertEquals(Coins.BITCOIN, event.getCoin());
        assertEquals(callbackQuery.getData(), event.getCoin().getIdsName());
        assertEquals(event.getChatId(), callbackQuery.getFrom().getId());
    }
}

package com.assettracker.main.telegram_bot.service;

import com.assettracker.main.telegram_bot.database.dto.UpdateDto;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.chat.Chat;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

public class UpdateUtilsTest {

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
    public void updateUtilsTest_getUser_UpdateWithMessage() {
        var update = getTooledUpdate_withMessage();

        User user = UpdateUtils.getUser(update);

        assertSame(update.getMessage().getFrom(), user);
    }

    @Test
    public void updateUtilsTest_getUser_UpdateWithCallbackQuery() {
        var update = getTooledUpdate_withCallbackQuery();

        User user = UpdateUtils.getUser(update);

        assertSame(update.getCallbackQuery().getFrom(), user);
    }

    @Test
    public void updateUtilsTest_getUserInput_withCorrectMessage() {
        var update = getTooledUpdate_withMessage();
        update.getMessage().setText("text");

        Optional<String> userInput = UpdateUtils.getUserInput(update);

        assertTrue(userInput.isPresent());
        assertEquals(userInput.get(), update.getMessage().getText());
    }

    @Test
    public void updateUtilsTest_getUserInput_withNullMessageText() {
        var update = getTooledUpdate_withMessage();
        update.getMessage().setText(null);

        Optional<String> userInput = UpdateUtils.getUserInput(update);

        assertEquals(userInput, Optional.empty());
    }

    @Test
    public void updateUtilsTest_getUserInput_withCallbackQuery() {
        var update = getTooledUpdate_withCallbackQuery();

        Optional<String> userInput = UpdateUtils.getUserInput(update);

        assertEquals(userInput, Optional.empty());
    }

    @Test
    public void updateUtilsTest_getChatId_withMessage() {
        var update = getTooledUpdate_withMessage();

        Long chatId = UpdateUtils.getChatId(update);

        assertNotNull(chatId);
        assertEquals(update.getMessage().getChatId(), chatId);
    }

    @Test
    public void updateUtilsTest_getChatId_withCallbackQuery() {
        var update = getTooledUpdate_withCallbackQuery();

        Long chatId = UpdateUtils.getChatId(update);

        assertNotNull(chatId);
        assertEquals(update.getCallbackQuery().getFrom().getId(), chatId);
    }

    @Test
    public void updateUtilsTest_toDto_withMessage() {
        var update = getTooledUpdate_withMessage();
        update.getMessage().setText("text");

        UpdateDto dto = UpdateUtils.toDto(update);

        assertNotNull(dto);
        assertTrue(dto.getUserInput().isPresent());
        assertEquals(update.getMessage().getText(), dto.getUserInput().get());
        assertEquals(update.getMessage().getFrom().getFirstName(), dto.getFirstName());
        assertEquals(update.getMessage().getFrom().getId(), dto.getChatId());
    }

    @Test
    public void updateUtilsTest_toDto_withCallbackQuery() {
        var update = getTooledUpdate_withCallbackQuery();

        UpdateDto dto = UpdateUtils.toDto(update);

        assertNotNull(dto);
        assertTrue(dto.getUserInput().isEmpty());
        assertEquals(update.getCallbackQuery().getFrom().getFirstName(), dto.getFirstName());
        assertEquals(update.getCallbackQuery().getFrom().getId(), dto.getChatId());
    }

}

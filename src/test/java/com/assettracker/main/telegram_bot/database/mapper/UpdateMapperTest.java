package com.assettracker.main.telegram_bot.database.mapper;

import com.assettracker.main.telegram_bot.database.dto.UpdateDto;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.chat.Chat;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class UpdateMapperTest {

    private final UpdateMapper mapper = new UpdateMapperImpl();

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
    public void toDtoTest() {
        Update update = getTooledUpdate_withMessage();
        update.getMessage().setText("text");

        UpdateDto dto = mapper.toDto(update);

        assertEquals(update.getMessage().getFrom().getFirstName(), dto.getFirstName());
        assertEquals(update.getMessage().getFrom().getUserName(), dto.getUserName());
        assertEquals(update.getMessage().getFrom().getLastName(), dto.getLastName());
        assertEquals(update.getMessage().getFrom().getId(), dto.getChatId());
        assertEquals(update.getMessage().getText(), dto.getUserInput().orElseThrow());
    }
}

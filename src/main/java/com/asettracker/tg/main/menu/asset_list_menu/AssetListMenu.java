package com.asettracker.tg.main.menu.asset_list_menu;

import com.asettracker.tg.main.config.ChatId;
import com.asettracker.tg.main.database.service.UserDbService;
import com.asettracker.tg.main.dto.UserStatus;
import com.asettracker.tg.main.menu.IButton;
import com.asettracker.tg.main.menu.IMenu;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.List;

@Component
@AllArgsConstructor
public class AssetListMenu implements IMenu {

    private final TelegramClient telegramClient;
    private final UserDbService userDbService;
    private List<IAssetListMenuButton> buttons;

    @SneakyThrows
    @Override
    public void sendMenu(Update update) {
        SendMessage sendMessage = SendMessage.builder()
                .chatId(ChatId.get(update))
                .text("Добавьте новую монету или обновите информацию о существующей:")
                .replyMarkup(combineButtons(buttons))
                .build();
        telegramClient.execute(sendMessage);
        userDbService.setStatus(
                ChatId.get(update),
                UserStatus.WAITING_FOR_NUMBER);
    }

    @Override
    public InlineKeyboardMarkup combineButtons(List<? extends IButton> buttons) {
        List<InlineKeyboardButton> but = buttons.stream().map(IButton::getButton).toList();
        return new InlineKeyboardMarkup(
                List.of(
                        new InlineKeyboardRow(but.get(0), but.get(1)),
                        new InlineKeyboardRow(but.get(2), but.get(3)),
                        new InlineKeyboardRow(but.get(4), but.get(5)),
                        new InlineKeyboardRow(but.get(6), but.get(7)),
                        new InlineKeyboardRow(but.get(8), but.get(9))
                )
        );
    }
}

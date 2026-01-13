package com.asettracker.tg.main.menu.bag_menu;

import com.asettracker.tg.main.database.dto.BagDto;
import com.asettracker.tg.main.database.service.BagDbService;
import com.asettracker.tg.main.menu.IMenu;
import com.asettracker.tg.main.service.ChatId;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.io.File;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class BagMenu implements IMenu {

    private static final String MENU_TEXT = getMenuText();
    private static final InputFile MENU_PHOTO = getMenuPhoto();
    private static final String KEY_PREFIX = "bag:";
    private final TelegramClient telegramClient;
    private final List<IBagMenuButton> buttons;
    private final BagDbService bagDbService;
    private final StringRedisTemplate redisTemplate;

    public static String getMenuText() {
        return """
                \uD83C\uDF92Информация о портфеле:
                ├ Создан: %s
                ├ Количество активов: %s
                
                \uD83D\uDCB0Суммарная стоимость:
                └ %s$""";
    }

    public static InputFile getMenuPhoto() {
        return new InputFile(
                new File("src/main/resources/static/your-bag.jpg")
        );
    }

    @SneakyThrows
    @Override
    public void sendMenu(Update update) {
        CompletableFuture<BagDto> bag = getBagAsync(update);
        SendPhoto sendPhoto = SendPhoto.builder()
                .chatId(ChatId.get(update))
                .photo(MENU_PHOTO)
                .caption(String.format(MENU_TEXT, bag.get().getCreatedAt(),
                        bag.get().getAssetCount(), bag.get().getTotalCost()))
                .replyMarkup(combineButtons(buttons))
                .build();
        telegramClient.execute(sendPhoto);
    }

    public CompletableFuture<BagDto> getBagAsync(Update update) {
        return CompletableFuture.supplyAsync(() ->
                bagDbService.findBagByChatId(ChatId.get(update))
                        .orElseThrow()
        );
    }
}

package com.assettracker.main.telegram_bot.events;

import com.assettracker.main.telegram_bot.buttons.menu.asset_list_menu.UserCoin;
import com.assettracker.main.telegram_bot.buttons.menu.bag_menu.BagMenu;
import com.assettracker.main.telegram_bot.buttons.menu.enter_asset_count_menu.EnterAssetCountMenu;
import com.assettracker.main.telegram_bot.buttons.menu.main_menu.MainMenu;
import com.assettracker.main.telegram_bot.database.service.BagService;
import com.assettracker.main.telegram_bot.database.service.DataInitializerService;
import com.assettracker.main.telegram_bot.service.AssetService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Service
@AllArgsConstructor
public class MessageEventHandler {

    private final ExecutorService es = Executors.newFixedThreadPool(10);
    private final MainMenu mainMenu;
    private final BagMenu bagMenu;
    private final BagService bagService;
    private final DataInitializerService initializer;
    private final AssetService assetService;
    private final EnterAssetCountMenu enterAssetCountMenu;

    @EventListener(condition = "event.getMessage().name() == 'START'")
//todo перед каждым проверить вдруг чел должен был ввести число, соответственоо сбросить tmp через AOP
    public void handleStart(MessageEvent event) {
        var chatId = event.getUpdateDto().getChatId();
        mainMenu.sendMenu(chatId);
        es.execute(() -> {
            if (bagService.findByChatId(chatId).isEmpty()) {
                initializer.initializeUserAndBag(event.getUpdateDto());
            }
        });
    }

    @EventListener(condition = "event.getMessage().name() == 'MENU'")
    public void handleMenu(MessageEvent event) {
        var chatId = event.getUpdateDto().getChatId();
        mainMenu.sendMenu(chatId);
    }

    @EventListener(condition = "event.getMessage().name() == 'BAG'")
    public void handleBag(MessageEvent event) {
        bagMenu.sendMenu(event.getUpdateDto().getChatId());
    }

    @EventListener(condition = "event.getMessage().name() == 'UNKNOWN'")
    public void handleUnknown(MessageEvent event) {
        var chatId = event.getUpdateDto().getChatId();
        if (assetService.isUserWaitingNumber(chatId)) {
            addAssetAndDeleteTmpUserCoin(event, chatId);
            enterAssetCountMenu.sendSuccess(chatId);
            bagMenu.sendMenu(chatId);
        }
    }

    private void addAssetAndDeleteTmpUserCoin(MessageEvent event, Long chatId) {
        var coinCount = BigDecimal.valueOf(
                Double.parseDouble(event.getUpdateDto().getUserInput().orElseThrow().trim())
        );
        UserCoin tmpCoin = assetService.getTmpUserCoin(chatId);
        tmpCoin.setCount(coinCount);
        bagService.addAsset(tmpCoin);
        assetService.deleteTmpUserCoin(chatId);
    }
}

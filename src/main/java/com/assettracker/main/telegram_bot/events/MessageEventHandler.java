package com.assettracker.main.telegram_bot.events;

import com.assettracker.main.telegram_bot.database.dto.UserQuestionDto;
import com.assettracker.main.telegram_bot.database.service.BagService;
import com.assettracker.main.telegram_bot.database.service.DataInitializerService;
import com.assettracker.main.telegram_bot.database.service.UserQuestionService;
import com.assettracker.main.telegram_bot.database.service.UserService;
import com.assettracker.main.telegram_bot.menu.asset_list_menu.UserCoin;
import com.assettracker.main.telegram_bot.menu.bag_menu.BagMenu;
import com.assettracker.main.telegram_bot.menu.enter_asset_count_menu.EnterAssetCountMenu;
import com.assettracker.main.telegram_bot.menu.main_menu.MainMenu;
import com.assettracker.main.telegram_bot.menu.my_profile_menu.MyProfileMenu;
import com.assettracker.main.telegram_bot.menu.support_menu.SupportMenu;
import com.assettracker.main.telegram_bot.menu.waiting_menu.WaitingMenu;
import com.assettracker.main.telegram_bot.service.AssetService;
import com.assettracker.main.telegram_bot.service.LastMessageService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Service
@AllArgsConstructor
public class MessageEventHandler {

    private final ExecutorService es = Executors.newFixedThreadPool(10);
    private final UserService userService;
    private final UserQuestionService userQuestionService;
    private final MainMenu mainMenu;
    private final BagMenu bagMenu;
    private final BagService bagService;
    private final DataInitializerService initializer;
    private final AssetService assetService;
    private final EnterAssetCountMenu enterAssetCountMenu;
    private final WaitingMenu waitingMenu;
    private final LastMessageService lastMessageService;
    private final MyProfileMenu myProfileMenu;
    private final SupportMenu supportMenu;

    @EventListener(condition = "event.getMessage().name() == 'START'")
    public void handleStart(MessageEvent event) {
        var chatId = event.getUpdateDto().getChatId();
        waitingMenu.sendMenu(chatId);
        Integer lastMessageId = lastMessageService.getLastMessage(event.getUpdateDto().getChatId());
        mainMenu.editMsgAndSendMenu(chatId, lastMessageId);
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
        waitingMenu.sendMenu(event.getUpdateDto().getChatId());
        Integer lastMessageId = lastMessageService.getLastMessage(event.getUpdateDto().getChatId());
        bagMenu.editMsgAndSendMenu(event.getUpdateDto().getChatId(), lastMessageId);
    }

    @EventListener(condition = "event.getMessage().name() == 'PROFILE'")
    public void handleProfile(MessageEvent event) {
        waitingMenu.sendMenu(event.getUpdateDto().getChatId());
        Integer lastMessageId = lastMessageService.getLastMessage(event.getUpdateDto().getChatId());
        myProfileMenu.editMsgAndSendMenu(event.getUpdateDto().getChatId(), lastMessageId);
    }

    @EventListener(condition = "event.getMessage().name() == 'UNKNOWN'")
    public void handleUnknown(MessageEvent event) {
        var chatId = event.getUpdateDto().getChatId();
        if (assetService.isUserWaitingNumber(chatId)) {
            addAssetAndDeleteTmpUserCoin(event, chatId);
            enterAssetCountMenu.sendSuccess(chatId);
            bagMenu.sendMenu(chatId);
        } else if (userService.isUserWriteQuestion(chatId)) {
            UUID userId = userService.findByChatId(chatId).orElseThrow().getId();
            userQuestionService.save(
                    new UserQuestionDto(event.getUpdateDto().getUserInput().orElseThrow(), userId)
            );
            supportMenu.sendSuccess(chatId);
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

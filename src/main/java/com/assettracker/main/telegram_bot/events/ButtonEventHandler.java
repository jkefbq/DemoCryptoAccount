package com.assettracker.main.telegram_bot.events;

import com.assettracker.main.telegram_bot.database.dto.BagDto;
import com.assettracker.main.telegram_bot.database.entity.UserStatus;
import com.assettracker.main.telegram_bot.database.service.BagService;
import com.assettracker.main.telegram_bot.database.service.UserService;
import com.assettracker.main.telegram_bot.menu.ai_advice_menu.AIAdviceMenu;
import com.assettracker.main.telegram_bot.menu.asset_list_menu.AssetDo;
import com.assettracker.main.telegram_bot.menu.asset_list_menu.AssetListMenu;
import com.assettracker.main.telegram_bot.menu.asset_list_menu.UserCoin;
import com.assettracker.main.telegram_bot.menu.asset_statistics_menu.AssetStatisticsMenu;
import com.assettracker.main.telegram_bot.menu.assets_menu.AssetsMenu;
import com.assettracker.main.telegram_bot.menu.bag_menu.BagMenu;
import com.assettracker.main.telegram_bot.menu.enter_asset_count_menu.EnterAssetCountMenu;
import com.assettracker.main.telegram_bot.menu.incorrect_create_asset_menu.IncorrectCreateAssetMenu;
import com.assettracker.main.telegram_bot.menu.incorrect_delete_menu.IncorrectDeleteMenu;
import com.assettracker.main.telegram_bot.menu.incorrect_update_asset_menu.IncorrectUpdateAssetMenu;
import com.assettracker.main.telegram_bot.menu.main_menu.MainMenu;
import com.assettracker.main.telegram_bot.menu.my_assets_menu.MyAssetsMenu;
import com.assettracker.main.telegram_bot.menu.my_profile_menu.MyProfileMenu;
import com.assettracker.main.telegram_bot.menu.support_menu.SupportMenu;
import com.assettracker.main.telegram_bot.menu.trade_with_ai_menu.TradeWithAIMenu;
import com.assettracker.main.telegram_bot.menu.waiting_menu.WaitingMenu;
import com.assettracker.main.telegram_bot.service.AssetService;
import com.assettracker.main.telegram_bot.service.LastMessageService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class ButtonEventHandler {

    private final BagMenu bagMenu;
    private final UserService userService;
    private final LastMessageService lastMessageService;
    private final MyAssetsMenu myAssetsMenu;
    private final BagService bagService;
    private final AssetService assetService;
    private final IncorrectUpdateAssetMenu incorrectUpdateAssetMenu;
    private final IncorrectCreateAssetMenu incorrectCreateAssetMenu;
    private final AssetListMenu assetListMenu;
    private final EnterAssetCountMenu enterAssetCountMenu;
    private final IncorrectDeleteMenu incorrectDeleteMenu;
    private final MyProfileMenu profileMenu;
    private final MainMenu mainMenu;
    private final WaitingMenu waitingMenu;
    private final AssetsMenu assetsMenu;
    private final AssetStatisticsMenu assetStatisticsMenu;
    private final TradeWithAIMenu tradeWithAIMenu;
    private final AIAdviceMenu aiAdviceMenu;
    private final SupportMenu supportMenu;

    @EventListener(condition = "event.getButton.name() == 'MY_BAG'")
    public void handleMyBag(ButtonEvent event) {
        Integer lastMessageId = lastMessageService.getLastMessage(event.getChatId());
        bagMenu.editMsgAndSendMenu(event.getChatId(), lastMessageId);
    }

    @EventListener(condition = "event.getButton().name() == 'CREATE_ASSET'")
    public void handleCreateAsset(ButtonEvent event) {
        Integer lastMessageId = lastMessageService.getLastMessage(event.getChatId());
        assetListMenu.editMsgAndSendMenu(event.getChatId(), lastMessageId);
        assetService.saveTmpUserCoin(new UserCoin(event.getChatId(), AssetDo.CREATE));
    }

    @EventListener(condition = "event.getButton().name() == 'UPDATE_ASSET'")
    public void handleUpdateAsset(ButtonEvent event) {
        Integer lastMessageId = lastMessageService.getLastMessage(event.getChatId());
        assetListMenu.editMsgAndSendMenu(event.getChatId(), lastMessageId);
        assetService.saveTmpUserCoin(new UserCoin(event.getChatId(), AssetDo.UPDATE));
    }

    @EventListener(condition = "event.getButton().name() == 'FORCE_UPDATE_ASSET'")
    public void handleForceUpdateAsset(ButtonEvent event) {
        UserCoin tmp = assetService.getTmpUserCoin(event.getChatId());
        tmp.setAssetDo(AssetDo.UPDATE);
        assetService.saveTmpUserCoin(tmp);
        Integer lastMessageId = lastMessageService.getLastMessage(event.getChatId());
        enterAssetCountMenu.editMsgAndSendMenu(event.getChatId(), lastMessageId);
    }

    @EventListener(condition = "event.getButton().name() == 'FORCE_CREATE_ASSET' || " +
            "event.getButton().name() == 'CREATE_ASSET_AFTER_TRY_DELETE'")
    public void handleForceCreateAsset(ButtonEvent event) {
        UserCoin tmp = assetService.getTmpUserCoin(event.getChatId());
        tmp.setAssetDo(AssetDo.CREATE);
        assetService.saveTmpUserCoin(tmp);
        Integer lastMessageId = lastMessageService.getLastMessage(event.getChatId());
        enterAssetCountMenu.editMsgAndSendMenu(event.getChatId(), lastMessageId);
    }

    @EventListener(condition = "event.getButton().name() == 'CANCEL_TO_MY_ASSETS'")
    public void handleCancelForceUpdateAsset(ButtonEvent event) {
        Integer lastMessageId = lastMessageService.getLastMessage(event.getChatId());
        myAssetsMenu.editMsgAndSendMenu(event.getChatId(), lastMessageId);
        assetService.deleteTmpUserCoin(event.getChatId());
    }

    @EventListener(condition = "event.getButton().name() == 'DELETE_ASSET'")
    public void handleDeleteAsset(ButtonEvent event) {
        Integer lastMessageId = lastMessageService.getLastMessage(event.getChatId());
        assetListMenu.editMsgAndSendMenu(event.getChatId(), lastMessageId);
        assetService.saveTmpUserCoin(new UserCoin(event.getChatId(), AssetDo.DELETE));
    }

    @EventListener(condition = "event.getButton().name() == 'CANCEL_TO_MENU'")
    public void handleCancelToMenu(ButtonEvent event) {
        if (userService.isUserWriteQuestion(event.getChatId())) {
            var user = userService.findByChatId(event.getChatId()).orElseThrow();
            user.setStatus(UserStatus.FREE);
            userService.saveUser(user);
        }
        Integer lastMessageId = lastMessageService.getLastMessage(event.getChatId());
        mainMenu.editMsgAndSendMenu(event.getChatId(), lastMessageId);
    }

    @EventListener(condition = "event.getButton().name() == 'MY_PROFILE'")
    public void handleProfile(ButtonEvent event) {
        waitingMenu.editMsgAndSendMenu(event.getChatId(), lastMessageService.getLastMessage(event.getChatId()));
        Integer lastMessageId = lastMessageService.getLastMessage(event.getChatId());
        profileMenu.editMsgAndSendMenu(event.getChatId(), lastMessageId);
    }

    @EventListener(condition = "event.getButton().name() == 'CANCEL_TO_BAG_MENU'")
    public void handleCancelToBagMenu(ButtonEvent event) {
        Integer lastMessageId = lastMessageService.getLastMessage(event.getChatId());
        bagMenu.editMsgAndSendMenu(event.getChatId(), lastMessageId);
    }

    @SneakyThrows
    @EventListener(condition = "event.getButton().name() == 'UPDATE_BAG_DATA'")
    public void handleUpdateBagData(ButtonEvent event) {
        BagDto bag = bagService.findByChatId(event.getChatId()).orElseThrow();
        bagService.actualizeBagFields(bag);

        waitingMenu.editMsgAndSendMenu(event.getChatId(), lastMessageService.getLastMessage(event.getChatId()));
        Integer lastMessageId = lastMessageService.getLastMessage(event.getChatId());
        bagMenu.editMsgAndSendMenu(event.getChatId(), lastMessageId);
    }

    @EventListener(condition = "event.getButton().name() == 'ASSETS' || " +
            "event.getButton().name() == 'CANCEL_TO_ASSETS'")
    public void handleAssets(ButtonEvent event) {
        Integer lastMessageId = lastMessageService.getLastMessage(event.getChatId());
        assetsMenu.editMsgAndSendMenu(event.getChatId(), lastMessageId);
    }

    @EventListener(condition = "event.getButton().name() == 'MY_ASSETS'")
    public void handleMyAssets(ButtonEvent event) {
        waitingMenu.editMsgAndSendMenu(event.getChatId(), lastMessageService.getLastMessage(event.getChatId()));
        Integer lastMessageId = lastMessageService.getLastMessage(event.getChatId());
        myAssetsMenu.editMsgAndSendMenu(event.getChatId(), lastMessageId);
    }

    @EventListener(condition = "event.getButton().name() == 'ASSET_STATISTICS'")
    public void handleAssetStatistics(ButtonEvent event) {
        waitingMenu.editMsgAndSendMenu(event.getChatId(), lastMessageService.getLastMessage(event.getChatId()));
        Integer lastMessageId = lastMessageService.getLastMessage(event.getChatId());
        assetStatisticsMenu.editMsgAndSendMenu(event.getChatId(), lastMessageId);
    }

    @EventListener(condition = "event.getButton().name() == 'TRADE_WITH_AI'")
    public void handleTradeWithAI(ButtonEvent event) {
        Integer lastMessageId = lastMessageService.getLastMessage(event.getChatId());
        tradeWithAIMenu.editMsgAndSendMenu(event.getChatId(), lastMessageId);
    }

    @EventListener(condition = "event.getButton().name() == 'AI_ADVICE'")
    public void handleAIAdvice(ButtonEvent event) {
        waitingMenu.sendMenu(event.getChatId());
        Integer lastMessageId = lastMessageService.getLastMessage(event.getChatId());
        aiAdviceMenu.editMsgAndSendMenu(event.getChatId(), lastMessageId);
    }

    @EventListener(condition = "event.getButton().name() == 'SUPPORT'")
    public void handleSupport(ButtonEvent event) {
        var user = userService.findByChatId(event.getChatId()).orElseThrow();
        user.setStatus(UserStatus.WRITING_QUESTION);
        userService.saveUser(user);
        supportMenu.sendMenu(event.getChatId());
    }

    @EventListener
    public void handleAnyAssetButton(AssetButtonEvent event) {
        var tmpCoin = assetService.getTmpUserCoin(event.getChatId());

        tmpCoin.setCoin(event.getCoin());
        waitingMenu.editMsgAndSendMenu(event.getChatId(), lastMessageService.getLastMessage(event.getChatId()));
        assetService.saveTmpUserCoin(tmpCoin);
        var lastMessageId = lastMessageService.getLastMessage(event.getChatId());
        processAsset(event, tmpCoin, lastMessageId);
    }

    private void processAsset(AssetButtonEvent event, UserCoin tmpCoin, Integer lastMessageId) {
        boolean hasCoin = bagService.hasCoin(event.getChatId(), event.getCoin());

        if (tmpCoin.getAssetDo() == AssetDo.CREATE && hasCoin) {
            incorrectCreateAssetMenu.editMsgAndSendMenu(event.getChatId(), lastMessageId);
        } else if (tmpCoin.getAssetDo() == AssetDo.UPDATE && !hasCoin) {
            incorrectUpdateAssetMenu.editMsgAndSendMenu(event.getChatId(), lastMessageId);
        } else if (tmpCoin.getAssetDo() == AssetDo.DELETE && hasCoin) {
            deleteAssetAndSendSuccess(event, tmpCoin, lastMessageId);
        } else if (tmpCoin.getAssetDo() == AssetDo.DELETE && !hasCoin) {
            incorrectDeleteMenu.editMsgAndSendMenu(tmpCoin.getChatId(), lastMessageId);
        } else {
            enterAssetCountMenu.editMsgAndSendMenu(tmpCoin.getChatId(), lastMessageId);
        }
    }

    private void deleteAssetAndSendSuccess(AssetButtonEvent event, UserCoin tmpCoin, Integer lastMessageId) {
        bagService.deleteAsset(tmpCoin.getChatId(), event.getCoin());
        assetService.deleteTmpUserCoin(tmpCoin.getChatId());
        incorrectDeleteMenu.EditMsgAndSendSuccess(tmpCoin.getChatId(), lastMessageId);
        bagMenu.sendMenu(event.getChatId());
    }
}

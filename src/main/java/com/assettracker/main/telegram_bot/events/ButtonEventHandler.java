package com.assettracker.main.telegram_bot.events;

import com.assettracker.main.telegram_bot.buttons.menu.asset_list_menu.AssetDo;
import com.assettracker.main.telegram_bot.buttons.menu.asset_list_menu.AssetListMenu;
import com.assettracker.main.telegram_bot.buttons.menu.asset_list_menu.UserCoin;
import com.assettracker.main.telegram_bot.buttons.menu.bag_menu.BagMenu;
import com.assettracker.main.telegram_bot.buttons.menu.enter_asset_count_menu.EnterAssetCountMenu;
import com.assettracker.main.telegram_bot.buttons.menu.incorrect_create_asset_menu.IncorrectCreateAssetMenu;
import com.assettracker.main.telegram_bot.buttons.menu.incorrect_delete_menu.IncorrectDeleteMenu;
import com.assettracker.main.telegram_bot.buttons.menu.incorrect_update_asset_menu.IncorrectUpdateAssetMenu;
import com.assettracker.main.telegram_bot.buttons.menu.manage_assets_menu.ManageAssetsMenu;
import com.assettracker.main.telegram_bot.database.service.BagService;
import com.assettracker.main.telegram_bot.service.AssetService;
import com.assettracker.main.telegram_bot.service.LastMessageService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class ButtonEventHandler {

    private final BagMenu bagMenu;
    private final LastMessageService lastMessageService;
    private final ManageAssetsMenu manageAssetsMenu;
    private final BagService bagService;
    private final AssetService assetService;
    private final IncorrectUpdateAssetMenu incorrectUpdateAssetMenu;
    private final IncorrectCreateAssetMenu incorrectCreateAssetMenu;
    private final AssetListMenu assetListMenu;
    private final EnterAssetCountMenu enterAssetCountMenu;
    private final IncorrectDeleteMenu incorrectDeleteMenu;

    @EventListener(condition = "event.getButton.name() == 'MY_BAG'")
    public void handleMyBag(ButtonEvent event) {
        Integer lastMessageId = lastMessageService.getLastMessage(event.getChatId());
        bagMenu.editMsgAndSendMenu(event.getChatId(), lastMessageId);
    }

    @EventListener(condition = "event.getButton().name() == 'MANAGE_ASSETS'")
    public void handleManageAssets(ButtonEvent event) {
        Integer lastMessageId = lastMessageService.getLastMessage(event.getChatId());
        manageAssetsMenu.editMsgAndSendMenu(event.getChatId(), lastMessageId);
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

    @EventListener(condition = "event.getButton().name() == 'CANCEL_TO_MANAGE_ASSETS'")
    public void handleCancelForceUpdateAsset(ButtonEvent event) {
        Integer lastMessageId = lastMessageService.getLastMessage(event.getChatId());
        manageAssetsMenu.editMsgAndSendMenu(event.getChatId(), lastMessageId);
        assetService.deleteTmpUserCoin(event.getChatId());
    }

    @EventListener(condition = "event.getButton().name() == 'DELETE_ASSET'")
    public void handleDeleteAsset(ButtonEvent event) {
        Integer lastMessageId = lastMessageService.getLastMessage(event.getChatId());
        assetListMenu.editMsgAndSendMenu(event.getChatId(), lastMessageId);
        assetService.saveTmpUserCoin(new UserCoin(event.getChatId(), AssetDo.DELETE));
    }

    @EventListener
    public void handleAnyAssetButton(AssetButtonEvent event) {
        var tmpCoin = assetService.getTmpUserCoin(event.getChatId());
        var lastMessageId = lastMessageService.getLastMessage(event.getChatId());

        tmpCoin.setCoin(event.getCoin());
        assetService.saveTmpUserCoin(tmpCoin);
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

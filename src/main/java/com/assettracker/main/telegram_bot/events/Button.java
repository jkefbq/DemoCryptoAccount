package com.assettracker.main.telegram_bot.events;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
public enum Button {
    MY_BAG("my_bag"),
    MY_PROFILE("my_profile"),
    ANY_ASSET("any_asset"),
    CREATE_ASSET("create_asset"),
    UPDATE_ASSET("update_asset"),
    DELETE_ASSET("delete_asset"),
    UPDATE_BAG_DATA("update_bag_data"),
    ASSETS("assets"),
    MY_ASSETS("my_assets"),
    TRADE_WITH_AI("trade_with_ai"),
    AI_ADVICE("ai_advice"),
    SUPPORT("support"),
    AI_QUESTION("ai_question"),
    CANCEL_TO_ASSETS("cancel_to_my_assets"),
    ASSET_STATISTICS("asset_statistics"),
    CANCEL_TO_BAG_MENU("cancel_to_bag_menu"),
    CANCEL_TO_MENU("cancel_to_menu"),
    CREATE_ASSET_AFTER_TRY_DELETE("create_asset_after_try_delete"),
    FORCE_UPDATE_ASSET("force_update_asset"),
    CANCEL_TO_MY_ASSETS("cancel_to_manage_assets"),
    FORCE_CREATE_ASSET("force_create_asset");

    @Getter
    private final String callbackData;

    public static Button parseCallbackData(String callbackData) {
        return Arrays.stream(Button.values())
                .filter(button -> button.getCallbackData().equals(callbackData))
                .findFirst()
                .orElseThrow();
    }
}

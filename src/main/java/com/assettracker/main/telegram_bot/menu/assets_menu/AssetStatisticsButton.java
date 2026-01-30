package com.assettracker.main.telegram_bot.menu.assets_menu;

import com.assettracker.main.telegram_bot.events.Button;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

@Component
@AllArgsConstructor
public class AssetStatisticsButton implements IAssetsMenuButton {

    @Getter
    private final String callbackData = Button.ASSET_STATISTICS.getCallbackData();

    @Override
    public InlineKeyboardButton getButton() {
        return InlineKeyboardButton.builder()
                .text("📊 Статистика")
                .callbackData(callbackData)
                .build();
    }
}

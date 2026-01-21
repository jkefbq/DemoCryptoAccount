package com.assettracker.main.telegram_bot.database.entity;

import com.assettracker.main.telegram_bot.buttons.menu.asset_list_menu.AssetDo;
import com.assettracker.main.telegram_bot.buttons.menu.asset_list_menu.Coins;
import com.assettracker.main.telegram_bot.buttons.menu.asset_list_menu.UserCoin;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Table(name = "user_coin")
@Entity
@NoArgsConstructor
@Setter
@Getter
public class UserCoinEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @Enumerated(EnumType.STRING)
    private Coins coin;

    private BigDecimal count;

    @Enumerated(EnumType.STRING)
    @Column(name = "asset_do")
    private AssetDo assetDo;

    @Column(name = "chat_id")
    private Long chatId;

    public UserCoinEntity(UserCoin userCoin) {
        this.id = userCoin.getId();
        this.coin = userCoin.getCoin();
        this.count = userCoin.getCount();
        this.chatId = userCoin.getChatId();
        this.assetDo = userCoin.getAssetDo();
    }
}



package com.assettracker.main.telegram_bot.database.entity;

import com.assettracker.main.telegram_bot.buttons.menu.asset_list_menu.Coins;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.MapKeyEnumerated;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Table(name = "bags")
@Entity
@Data
@NoArgsConstructor
public class BagEntity implements Serializable {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    private Long chatId;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "total_cost")
    private BigDecimal totalCost;

    @Column(name = "asset_count")
    private Integer assetCount;

    @Version
    private Long version;

    @ElementCollection(fetch = FetchType.EAGER)
    @MapKeyEnumerated(EnumType.STRING)
    @CollectionTable(name = "assets", joinColumns = @JoinColumn(name = "bag_id"))
    @MapKeyColumn(name = "coin_name")
    @Column(name = "coin_count")
    private Map<Coins, BigDecimal> assets;

    public BagEntity(Long chatId) {
        this.createdAt = LocalDate.now();
        this.totalCost = BigDecimal.ZERO;
        this.assetCount = 0;
        this.chatId = chatId;
        this.assets = new HashMap<>();
    }
}

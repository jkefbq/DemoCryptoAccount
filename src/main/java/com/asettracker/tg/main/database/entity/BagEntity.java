package com.asettracker.tg.main.database.entity;

import com.asettracker.tg.main.config.ChatId;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Table(name = "bags")
@Entity
@Getter
@NoArgsConstructor
public class BagEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    private Long chatId;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Setter
    @Column(name = "total_cost")
    private BigDecimal totalCost;

    @Setter
    @Column(name = "asset_count")
    private int assetCount;

    @ElementCollection
    @CollectionTable(name = "assets", joinColumns = @JoinColumn(name = "bag_id"))
    @MapKeyColumn(name = "asset_key")
    @Column(name = "asset_value")
    private Map<String, Double> assets = new HashMap<>();

    public BagEntity(Update update) {
        this.createdAt = LocalDate.now();
        this.totalCost = BigDecimal.ZERO;
        this.assetCount = 0;
        this.chatId = ChatId.get(update);
    }
}

package com.asettracker.tg.main.database.entity;

import com.asettracker.tg.main.service.ChatId;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Table(name = "bags")
@Entity
@Getter
@ToString
@NoArgsConstructor
public class BagEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @Setter
    private Long chatId;

    @Setter
    @Column(name = "created_at")
    private LocalDate createdAt;

    @Setter
    @Column(name = "total_cost")
    private BigDecimal totalCost;

    @Setter
    @Column(name = "asset_count")
    private int assetCount;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "assets", joinColumns = @JoinColumn(name = "bag_id"))
    @MapKeyColumn(name = "asset_key")
    @Column(name = "asset_value")
    private Map<String, Double> assets;

    public BagEntity(Update update) {
        this.createdAt = LocalDate.now();
        this.totalCost = BigDecimal.ZERO;
        this.assetCount = 0;
        this.chatId = ChatId.get(update);
        this.assets = new HashMap<>();
    }

    public BagEntity(Long chatId) {
        this.createdAt = LocalDate.now();
        this.totalCost = BigDecimal.ZERO;
        this.assetCount = 0;
        this.chatId = chatId;
        this.assets = new HashMap<>();
    }
}

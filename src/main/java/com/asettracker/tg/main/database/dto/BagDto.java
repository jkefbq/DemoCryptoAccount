package com.asettracker.tg.main.database.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BagDto {
    private UUID id;
    private Long chatId;
    private LocalDate createdAt;
    private BigDecimal totalCost;
    private Integer assetCount;
    private Long version;
    private Map<String, BigDecimal> assets;
}

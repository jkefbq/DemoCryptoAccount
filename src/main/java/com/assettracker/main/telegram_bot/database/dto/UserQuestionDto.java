package com.assettracker.main.telegram_bot.database.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserQuestionDto {
    private UUID id;
    private UUID userId;
    private String question;
    private Integer version;
    private LocalDateTime createdAt;
    private Boolean replied;

    public UserQuestionDto(String question, UUID userId) {
        this.question = question;
        this.userId = userId;
        this.createdAt = LocalDateTime.now();
        this.replied = false;
    }
}

package com.assettracker.main.telegram_bot.database.mapper;

import com.assettracker.main.telegram_bot.database.dto.UserQuestionDto;
import com.assettracker.main.telegram_bot.database.entity.UserQuestionEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserQuestionMapper {
    UserQuestionEntity toEntity(UserQuestionDto dto);
    UserQuestionDto toDto(UserQuestionEntity entity);
}

package com.asettracker.tg.main.database.mapper;

import com.asettracker.tg.main.database.dto.UserDto;
import com.asettracker.tg.main.database.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(UserEntity entity);
    UserEntity toEntity(UserDto dto);
}

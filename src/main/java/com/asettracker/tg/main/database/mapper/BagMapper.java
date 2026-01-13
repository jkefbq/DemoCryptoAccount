package com.asettracker.tg.main.database.mapper;

import com.asettracker.tg.main.database.dto.BagDto;
import com.asettracker.tg.main.database.entity.BagEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BagMapper {
    BagDto toDto(BagEntity entity);
    BagEntity toEntity(BagDto dto);
}

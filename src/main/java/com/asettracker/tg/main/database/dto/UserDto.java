package com.asettracker.tg.main.database.dto;

import com.asettracker.tg.main.database.UserStatus;
import lombok.Data;

import java.util.UUID;

@Data
public class UserDto {
    private UUID id;
    private Long chatId;
    private String firstName;
    private String lastName;
    private String userName;
    private UserStatus status;

}

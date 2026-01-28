package com.assettracker.main.telegram_bot.database.entity;

import com.assettracker.main.telegram_bot.dto.UpdateDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
@Setter
public class UserEntity implements Serializable {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    private Long chatId;

    @Column(name = "first_name", length = 35)
    private String firstName;

    @Column(name = "last_name", length = 45)
    private String lastName;

    @Column(name = "username", length = 45)
    private String userName;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "bag_id")
    private BagEntity bag;

    public UserEntity(UpdateDto updateDto) {
        this.status = UserStatus.FREE;
        this.firstName = updateDto.getFirstName();
        this.lastName = updateDto.getLastName();
        this.userName = updateDto.getUserName();
        this.chatId = updateDto.getChatId();
    }
}

package com.asettracker.tg.main.database.entity;

import com.asettracker.tg.main.config.ChatId;
import com.asettracker.tg.main.dto.UserStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    private Long chatId;

    @Column(name = "first_name", length = 35)
    private String firstName;

    @Column(name = "last_name", length = 45)
    private String lastName;

    @Column(name = "username", length = 45)
    private String userName;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private UserStatus userStatus;

    @Setter
    @OneToOne
    @JoinColumn(name = "bag_id")
    private BagEntity bag;

    public UserEntity(Update update) {
        this.firstName = ChatId.getUser(update).getFirstName();
        this.lastName = ChatId.getUser(update).getLastName();
        this.userName = ChatId.getUser(update).getUserName();
        this.chatId = ChatId.get(update);
        this.userStatus = UserStatus.FREE;
    }
}

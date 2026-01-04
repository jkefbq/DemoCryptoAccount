package com.asettracker.tg.main.menu.asset_list_menu;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
public class UserChoose {
    @Setter
    private String coinName;
    private Double coinCount;

    public void setCoinCount(Double coinCount) {
        if (coinCount < 0) {
            throw new IllegalArgumentException("coin count must be greater then 0");
        }
        this.coinCount = coinCount;
    }
}

package com.asettracker.tg.main.menu.asset_list_menu;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserChoose {
    @Setter
    private String coinName;
    private BigDecimal coinCount;

    public void setCoinCount(BigDecimal coinCount) {
        if (coinCount.compareTo(BigDecimal.ZERO) > 0) {
            throw new IllegalArgumentException("coin count must be greater then 0");
        }
        this.coinCount = coinCount;
    }
}

package com.assettracker.main.telegram_bot.admin;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {

    @GetMapping("/greeting")
    public String getGreeting(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        System.out.println(userDetails);
        return "success";
    }
}

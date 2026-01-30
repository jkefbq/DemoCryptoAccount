package com.assettracker.main.telegram_bot.controller;

import com.assettracker.main.telegram_bot.database.dto.UserQuestionDto;
import com.assettracker.main.telegram_bot.database.service.UserQuestionService;
import com.assettracker.main.telegram_bot.database.service.UserService;
import com.assettracker.main.telegram_bot.menu.support_menu.SupportMenu;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.UUID;

@Slf4j
@Controller
@AllArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/admin")
public class AdminController {

    private final UserQuestionService userQuestionService;
    private final UserService userService;
    private final SupportMenu supportMenu;

    @GetMapping
    public String getMainMenu() {
        log.info("call /admin");
        return "admin";
    }

    @ResponseBody
    @GetMapping("/questions")
    public List<UserQuestionDto> getAllQuestions() {
        log.info("call /admin/questions");
        return userQuestionService.getAllUserQuestions();
    }

    @ResponseBody
    @PostMapping("/reply")
    public ResponseEntity<?> sendReply(
            @RequestParam UUID id, @RequestParam String answer
    ) {
        log.info("call /admin/reply");
        var userId = userQuestionService.findById(id).orElseThrow().getUserId();
        var chatId = userService.findById(userId).orElseThrow().getChatId();
        supportMenu.sendAnswer(chatId, answer);
        userQuestionService.deleteById(id);
        return ResponseEntity.ok().build();
    }

}


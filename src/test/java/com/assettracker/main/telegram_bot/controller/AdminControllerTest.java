package com.assettracker.main.telegram_bot.controller;

import com.assettracker.main.telegram_bot.config.security.AdminUserService;
import com.assettracker.main.telegram_bot.config.security.SecurityConfig;
import com.assettracker.main.telegram_bot.database.dto.UserDto;
import com.assettracker.main.telegram_bot.database.dto.UserQuestionDto;
import com.assettracker.main.telegram_bot.database.service.UserQuestionService;
import com.assettracker.main.telegram_bot.database.service.UserService;
import com.assettracker.main.telegram_bot.menu.support_menu.SupportMenu;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(AdminController.class)
@Import({SecurityConfig.class, AdminUserService.class})
public class AdminControllerTest {

    @MockitoBean
    UserQuestionService userQuestionService;
    @MockitoBean
    UserService userService;
    @MockitoBean
    SupportMenu supportMenu;

    @Autowired
    MockMvc mockMvc;

    @Test
    public void getMainMenuTest_adminRole() throws Exception {
        mockMvc.perform(get("/admin").with(user("test").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(view().name("admin"));
    }

    @Test
    public void getMainMenuTest_userRole() throws Exception {
        mockMvc.perform(get("/admin").with(user("test").roles("USER")))
                .andExpect(status().isForbidden());
    }

    @Test
    public void getAllQuestionsTest_adminRole() throws Exception {
        mockMvc.perform(get("/admin/questions").with(user("test").roles("ADMIN")))
                .andExpect(status().isOk());
        verify(userQuestionService, times(1)).getAllUserQuestions();
    }

    @Test
    public void getAllQuestionsTest_userRole() throws Exception {
        mockMvc.perform(get("/admin/questions").with(user("test").roles("USER")))
                .andExpect(status().isForbidden());
    }

    @Test
    public void sendReplyTest_adminRole() throws Exception {
        when(userQuestionService.findById(any())).thenReturn(Optional.of(new UserQuestionDto("", UUID.randomUUID())));
        when(userService.findById(any())).thenReturn(Optional.of(new UserDto()));
        mockMvc.perform(post("/admin/reply")
                        .with(user("test").roles("ADMIN"))
                        .with(csrf())
                        .param("id", UUID.randomUUID().toString())
                        .param("answer", "my answer"))
                .andExpect(status().isOk());
        verify(supportMenu).sendAnswer(any(), any());
    }

    @Test
    public void sendReplyTest_userRole() throws Exception {
        when(userQuestionService.findById(any())).thenReturn(Optional.of(new UserQuestionDto("", UUID.randomUUID())));
        when(userService.findById(any())).thenReturn(Optional.of(new UserDto()));
        mockMvc.perform(post("/admin/reply")
                        .with(user("test").roles("USER"))
                        .with(csrf())
                        .param("id", UUID.randomUUID().toString())
                        .param("answer", "my answer"))
                .andExpect(status().isForbidden());
    }
}

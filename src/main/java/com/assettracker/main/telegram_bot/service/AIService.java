package com.assettracker.main.telegram_bot.service;

import com.assettracker.main.telegram_bot.menu.asset_list_menu.Coins;
import com.google.genai.Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

@Component
@Service
public class AIService {

    private static final String AI_MODEL = "gemini-2.5-flash";
    private static final String ADVICE_PROMPT;
    private static final String ADDITION;
    private  final Client client;

    static {
        try {
            ADVICE_PROMPT = Files.readString(Path.of("src/main/resources/static/ai-advice-prompt.txt"));
            ADDITION = Files.readString(Path.of("src/main/resources/static/ai-addition.txt"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public AIService(@Value("${GEMINI_API_KEY}") String GEMINI_API_KEY) {
        client = Client.builder().apiKey(GEMINI_API_KEY).build();
    }

    public String getAdvice(Map<Coins, BigDecimal> coins) {
        return client.models.generateContent(
                AI_MODEL,
                ADVICE_PROMPT + coins.toString(),
                null
        ).text();
    }

    public String getAnswer(String question, Map<Coins, BigDecimal> coins) {
        return client.models.generateContent(
                AI_MODEL,
                question + ADDITION + coins,
                null
        ).text();
    }
}

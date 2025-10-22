package com.cs203.core.service.impl;

import com.cs203.core.dto.responses.NewsArticlesResponseDTO;
import com.cs203.core.entity.NewsArticleEntity;
import com.cs203.core.repository.NewsRepository;
import com.cs203.core.service.NewsService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.ResponseFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class NewsServiceImpl implements NewsService {
    private final Logger logger = LoggerFactory.getLogger(NewsServiceImpl.class);

    private final NewsRepository newsRepository;
    private final ChatModel chatModel;

    @Autowired
    public NewsServiceImpl(NewsRepository newsRepository, ChatModel chatModel) {
        this.newsRepository = newsRepository;
        this.chatModel = chatModel;
    }

    @PostConstruct
    @Profile("!test")
    // https://stackoverflow.com/questions/50342929/how-to-automatically-disable-a-spring-bean-when-running-a-unit-test
    @Scheduled(cron = "0 0 0 * * ?")
    protected void scrapeNews() {
        // no‑op if provider key not configured
        if (System.getenv("PERPLEXITY_KEY") == null &&
                System.getProperty("PERPLEXITY_KEY") == null) {
            logger.info("PERPLEXITY_KEY not set; skipping scrape.");
            return;
        }
        ChatResponse response = chatModel.call(
                new Prompt(
                        "You are a helpful assistant. Provide exactly 2 of latest trade-related news (tariffs, trade agreements, exchange rates). \n" +
                                "Output ONLY a valid JSON array. \n" +
                                "Each element must have exactly these fields: \n" +
                                "  - headline (string)\n" +
                                "  - summary (string)\n" +
                                "  - url (string)\n" +
                                "  - tags (array of strings, e.g. ['tariff', 'US-China', 'steel']) \n" +
                                "Do not include any other text.",
                        OpenAiChatOptions.builder()
                                .model("sonar")
                                .maxTokens(500) // blast it once we ready
                                .responseFormat(
                                        ResponseFormat.builder()
                                                .type(ResponseFormat.Type.JSON_SCHEMA)
                                                .jsonSchema("""
                                                        {
                                                          "type": "array",
                                                          "items": {
                                                            "type": "object",
                                                            "properties": {
                                                              "headline": {"type": "string"},
                                                              "summary": {"type": "string"},
                                                              "url": {"type": "string"},
                                                              "tags": {
                                                                "type": "array",
                                                                "items": {"type": "string"}
                                                              }
                                                            },
                                                            "required": ["headline","summary","url","tags"]
                                                          }
                                                        }
                                                        """)
                                                .build()
                                )
                                .build()
                )
        );
        String responseText = response.getResult().getOutput().getText();
        logger.info(responseText);

        List<NewsArticleEntity> newsArticleEntities = parseAIResponseJson(responseText);
        newsRepository.saveAll(newsArticleEntities);

    }

    // remove truncated response entity in case token count cockblock us
    public List<NewsArticleEntity> parseAIResponseJson(String text) {
        ObjectMapper mapper = new ObjectMapper();
        while (!text.isEmpty()) {
            try {
                return mapper.readValue(text, new TypeReference<List<NewsArticleEntity>>() {
                });
            } catch (Exception e) {
                int lastIndex = text.lastIndexOf("},");
                if (lastIndex == -1) {
                    return Collections.emptyList();
                }
                text = text.substring(0, lastIndex + 1) + "]";
            }
        }
        return Collections.emptyList();
    }

    @Override
    public NewsArticlesResponseDTO fetchLatestNews() {
        List<NewsArticleEntity> news = newsRepository.findAll();
        return new NewsArticlesResponseDTO(news);
    }

    @Override
    public NewsArticlesResponseDTO fetchLatestNewsByTags(List<String> tags) {
        List<NewsArticleEntity> news = newsRepository.findNewsArticleEntitiesByTags(tags);
        return new NewsArticlesResponseDTO(news);
    }
}

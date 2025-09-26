package com.cs203.core.service.impl;

import com.cs203.core.dto.responses.NewsArticlesResponseDTO;
import com.cs203.core.entity.NewsArticleEntity;
import com.cs203.core.repository.NewsRepository;
import com.cs203.core.service.NewsService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.ResponseFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class NewsServiceImpl implements NewsService {
    private final Logger logger = LoggerFactory.getLogger(NewsServiceImpl.class);

    private final NewsRepository newsRepository;
    private final ChatModel chatModel;

    @Autowired
    public NewsServiceImpl(NewsRepository newsRepository, ChatModel chatModel) {
        this.newsRepository = newsRepository;
        this.chatModel = chatModel;
    }

    @Override
    public void scrapeNews() {
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
                                .maxTokens(200) // blast it once we ready
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

        ObjectMapper mapper = new ObjectMapper();
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
                text = text.substring(0, lastIndex + 1);
            }
        }
        return Collections.emptyList();
    }

    @Override
    public NewsArticlesResponseDTO fetchLatestNews() {
        return null;
    }

    @Override
    public NewsArticlesResponseDTO fetchLatestNewsByCountry(String country) {
        return null;
    }
}

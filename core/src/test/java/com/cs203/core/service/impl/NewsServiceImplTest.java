package com.cs203.core.service.impl;

import com.cs203.core.dto.responses.NewsArticlesResponseDTO;
import com.cs203.core.entity.NewsArticleEntity;
import com.cs203.core.repository.NewsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NewsServiceImplTest {

    @Mock
    private NewsRepository newsRepository;

    @Mock
    private ChatModel chatModel;

    @Mock
    private ChatResponse chatResponse;

    @Mock
    private Generation generation;

    @Captor
    private ArgumentCaptor<List<NewsArticleEntity>> newsCaptor;

    @InjectMocks
    private NewsServiceImpl newsService;

    @BeforeEach
    void setUp() {
        // Common setup can go here if needed
    }

    @Test
    void scrapeNews_shouldSuccessfullyParseAndSaveNews() {
        // Arrange
        String mockJsonResponse = """
                [
                  {
                    "headline": "US Imposes New Tariffs on Steel",
                    "summary": "The United States announced new tariffs on imported steel.",
                    "url": "https://example.com/news1",
                    "tags": ["tariff", "US", "steel"]
                  },
                  {
                    "headline": "China Trade Agreement Signed",
                    "summary": "A new trade agreement between China and EU was signed.",
                    "url": "https://example.com/news2",
                    "tags": ["trade-agreement", "China", "EU"]
                  }
                ]
                """;

        when(chatModel.call(any(Prompt.class))).thenReturn(chatResponse);
        when(chatResponse.getResult()).thenReturn(generation);
        when(generation.getOutput()).thenReturn(new AssistantMessage(mockJsonResponse));

        // Act
        newsService.scrapeNews();

        // Assert
        verify(chatModel, times(1)).call(any(Prompt.class));
        verify(newsRepository, times(1)).saveAll(newsCaptor.capture());

        List<NewsArticleEntity> savedNews = newsCaptor.getValue();
        assertEquals(2, savedNews.size());
        assertEquals("US Imposes New Tariffs on Steel", savedNews.get(0).getHeadline());
        assertEquals("https://example.com/news1", savedNews.get(0).getUrl());
        assertEquals(3, savedNews.get(0).getTags().size());
        assertTrue(savedNews.get(0).getTags().contains("tariff"));
    }

    @Test
    void scrapeNews_shouldHandleEmptyResponse() {
        // Arrange
        String mockJsonResponse = "[]";

        when(chatModel.call(any(Prompt.class))).thenReturn(chatResponse);
        when(chatResponse.getResult()).thenReturn(generation);
        when(generation.getOutput()).thenReturn(new AssistantMessage(mockJsonResponse));

        // Act
        newsService.scrapeNews();

        // Assert
        verify(newsRepository, times(1)).saveAll(newsCaptor.capture());
        List<NewsArticleEntity> savedNews = newsCaptor.getValue();
        assertTrue(savedNews.isEmpty());
    }

    @Test
    void parseAIResponseJson_shouldParseValidJson() {
        // Arrange
        String validJson = """
                [
                  {
                    "headline": "Test Headline",
                    "summary": "Test Summary",
                    "url": "https://test.com",
                    "tags": ["test", "tag"]
                  }
                ]
                """;

        // Act
        List<NewsArticleEntity> result = newsService.parseAIResponseJson(validJson);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Headline", result.get(0).getHeadline());
        assertEquals("Test Summary", result.get(0).getSummary());
        assertEquals("https://test.com", result.get(0).getUrl());
        assertEquals(2, result.get(0).getTags().size());
    }

    @Test
    void parseAIResponseJson_shouldParseMultipleArticles() {
        // Arrange
        String validJson = """
                [
                  {
                    "headline": "Article 1",
                    "summary": "Summary 1",
                    "url": "https://test1.com",
                    "tags": ["tag1"]
                  },
                  {
                    "headline": "Article 2",
                    "summary": "Summary 2",
                    "url": "https://test2.com",
                    "tags": ["tag2", "tag3"]
                  }
                ]
                """;

        // Act
        List<NewsArticleEntity> result = newsService.parseAIResponseJson(validJson);

        // Assert
        assertEquals(2, result.size());
        assertEquals("Article 1", result.get(0).getHeadline());
        assertEquals("Article 2", result.get(1).getHeadline());
    }

    @Test
    void parseAIResponseJson_shouldHandleTruncatedJson() {
        // Arrange - Missing closing bracket
        String truncatedJson = """
                [
                  {
                    "headline": "Complete Article",
                    "summary": "Complete Summary",
                    "url": "https://complete.com",
                    "tags": ["complete"]
                  },
                  {
                    "headline": "Incomplete Article",
                    "summary": "This is trunca
                """;

        // Act
        List<NewsArticleEntity> result = newsService.parseAIResponseJson(truncatedJson);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Complete Article", result.get(0).getHeadline());
    }

    @Test
    void parseAIResponseJson_shouldHandleTruncatedJsonWithMultipleValidEntries() {
        // Arrange
        String truncatedJson = """
                [
                  {
                    "headline": "Article 1",
                    "summary": "Summary 1",
                    "url": "https://test1.com",
                    "tags": ["tag1"]
                  },
                  {
                    "headline": "Article 2",
                    "summary": "Summary 2",
                    "url": "https://test2.com",
                    "tags": ["tag2"]
                  },
                  {
                    "headline": "Article 3 Incomplete
                """;

        // Act
        List<NewsArticleEntity> result = newsService.parseAIResponseJson(truncatedJson);

        // Assert
        assertEquals(2, result.size());
    }

    @Test
    void parseAIResponseJson_shouldReturnEmptyListForInvalidJson() {
        // Arrange
        String invalidJson = "This is not JSON at all";

        // Act
        List<NewsArticleEntity> result = newsService.parseAIResponseJson(invalidJson);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void parseAIResponseJson_shouldReturnEmptyListForEmptyString() {
        // Arrange
        String emptyString = "";

        // Act
        List<NewsArticleEntity> result = newsService.parseAIResponseJson(emptyString);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void parseAIResponseJson_shouldReturnEmptyListForCompletelyMalformedJson() {
        // Arrange
        String malformedJson = "{{{[[broken";

        // Act
        List<NewsArticleEntity> result = newsService.parseAIResponseJson(malformedJson);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void fetchLatestNews_shouldReturnAllNews() {
        // Arrange
        NewsArticleEntity news1 = new NewsArticleEntity();
        news1.setHeadline("News 1");
        NewsArticleEntity news2 = new NewsArticleEntity();
        news2.setHeadline("News 2");

        List<NewsArticleEntity> mockNews = Arrays.asList(news1, news2);
        when(newsRepository.findAll()).thenReturn(mockNews);

        // Act
        NewsArticlesResponseDTO result = newsService.fetchLatestNews();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.articles().size());
        verify(newsRepository, times(1)).findAll();
    }

    @Test
    void fetchLatestNewsByTags_shouldReturnFilteredNews() {
        // Arrange
        List<String> tags = Arrays.asList("tariff", "US");
        NewsArticleEntity news1 = new NewsArticleEntity();
        news1.setHeadline("Tariff News");
        news1.setTags(tags);

        List<NewsArticleEntity> mockNews = Collections.singletonList(news1);
        when(newsRepository.findNewsArticleEntitiesByTags(tags)).thenReturn(mockNews);

        // Act
        NewsArticlesResponseDTO result = newsService.fetchLatestNewsByTags(tags);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.articles().size());
        assertEquals("Tariff News", result.articles().get(0).getHeadline());
        verify(newsRepository, times(1)).findNewsArticleEntitiesByTags(tags);
    }

    @Test
    void fetchLatestNewsByTags_shouldReturnEmptyListWhenNoMatchingTags() {
        // Arrange
        List<String> tags = Arrays.asList("nonexistent");
        when(newsRepository.findNewsArticleEntitiesByTags(tags)).thenReturn(Collections.emptyList());

        // Act
        NewsArticlesResponseDTO result = newsService.fetchLatestNewsByTags(tags);

        // Assert
        assertNotNull(result);
        assertTrue(result.articles().isEmpty());
        verify(newsRepository, times(1)).findNewsArticleEntitiesByTags(tags);
    }
}
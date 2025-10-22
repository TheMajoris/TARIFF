package com.cs203.core.controller;

import com.cs203.core.dto.responses.NewsArticlesResponseDTO;
import com.cs203.core.entity.NewsArticleEntity;
import com.cs203.core.service.NewsService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyList;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
class NewsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private NewsService newsService;

    @Test
    @DisplayName("GET /api/v1/news should return all latest news")
    void getNews_ShouldReturnLatestNews() throws Exception {
        // Arrange
        NewsArticleEntity article1 = new NewsArticleEntity();
        article1.setHeadline("Trade Tensions Rising");
        article1.setSummary("Tariffs increase between US and China");
        article1.setUrl("http://example.com/trade");
        article1.setTags(List.of("tariff", "US-China"));

        NewsArticleEntity article2 = new NewsArticleEntity();
        article2.setHeadline("Currency Fluctuations");
        article2.setSummary("USD weakens against EUR");
        article2.setUrl("http://example.com/currency");
        article2.setTags(List.of("currency", "exchange"));

        NewsArticlesResponseDTO responseDTO = new NewsArticlesResponseDTO(List.of(article1, article2));
        Mockito.when(newsService.fetchLatestNews()).thenReturn(responseDTO);

        // Act + Assert
        mockMvc.perform(get("/api/v1/news"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.articles", hasSize(2)))
                .andExpect(jsonPath("$.articles[0].headline").value("Trade Tensions Rising"))
                .andExpect(jsonPath("$.articles[1].tags", hasItem("currency")));

        Mockito.verify(newsService).fetchLatestNews();
    }

    @Test
    @DisplayName("GET /api/v1/news?tags=tariff,currency should return filtered news")
    void getNewsByTags_ShouldReturnFilteredNews() throws Exception {
        // Arrange
        NewsArticleEntity article = new NewsArticleEntity();
        article.setHeadline("Trade Tariffs");
        article.setSummary("Tariff increase announced");
        article.setUrl("http://example.com/tariff");
        article.setTags(List.of("tariff"));

        NewsArticlesResponseDTO filtered = new NewsArticlesResponseDTO(List.of(article));
        Mockito.when(newsService.fetchLatestNewsByTags(anyList())).thenReturn(filtered);

        // Act + Assert
        mockMvc.perform(get("/api/v1/news")
                        .param("tags", "tariff", "currency"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.articles", hasSize(1)))
                .andExpect(jsonPath("$.articles[0].headline").value("Trade Tariffs"));

        Mockito.verify(newsService).fetchLatestNewsByTags(List.of("tariff", "currency"));
    }

    @Test
    @DisplayName("GET /api/v1/news?tags= should fail validation for empty tags list")
    void getNewsByTags_ShouldFailOnEmptyTags() throws Exception {
        mockMvc.perform(get("/api/v1/news")
                        .param("tags", ""))
                .andExpect(status().isBadRequest());
    }
}
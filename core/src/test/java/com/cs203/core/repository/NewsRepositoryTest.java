package com.cs203.core.repository;

import com.cs203.core.entity.NewsArticleEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:postgresql://localhost:5432/tariff_db",
        "spring.datasource.username=admin",
        "spring.datasource.password=admin123",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.show-sql=false",
        "spring.sql.init.mode=never"
})
class NewsRepositoryTest {
    @Autowired
    private NewsRepository newsRepository;

    @Test
    @DisplayName("findNewsArticleEntitiesByTags() should return articles containing any of the given tags")
    void testFindNewsArticleEntitiesByTags() {
        // Arrange
        NewsArticleEntity article1 = new NewsArticleEntity();
        article1.setHeadline("Trade Tariffs Rising");
        article1.setSummary("Tariffs increase between US and China");
        article1.setUrl("http://example.com/tariffs");
        article1.setTags(List.of("tariff", "US-China"));

        NewsArticleEntity article2 = new NewsArticleEntity();
        article2.setHeadline("Currency Market Fluctuations");
        article2.setSummary("USD weakens against EUR");
        article2.setUrl("http://example.com/currency");
        article2.setTags(List.of("currency", "exchange"));

        NewsArticleEntity article3 = new NewsArticleEntity();
        article3.setHeadline("Unrelated Tech News");
        article3.setSummary("New phone released");
        article3.setUrl("http://example.com/tech");
        article3.setTags(List.of("technology", "gadgets"));

        newsRepository.saveAll(List.of(article1, article2, article3));

        // Act
        List<NewsArticleEntity> result = newsRepository.findNewsArticleEntitiesByTags(List.of("tariff", "currency"));

        // Assert
        assertEquals(2, result.size());
        assertThat(result)
                .extracting(NewsArticleEntity::getHeadline)
                .containsExactlyInAnyOrder("Trade Tariffs Rising", "Currency Market Fluctuations");
    }

    @Test
    @DisplayName("findNewsArticleEntitiesByTags() should return empty when no matching tags")
    void testFindNewsArticleEntitiesByTags_NoMatches() {
        // Arrange
        NewsArticleEntity article = new NewsArticleEntity();
        article.setHeadline("Unrelated");
        article.setSummary("Summary");
        article.setUrl("http://example.com");
        article.setTags(List.of("sports", "entertainment"));
        newsRepository.save(article);

        // Act
        List<NewsArticleEntity> result = newsRepository.findNewsArticleEntitiesByTags(List.of("tariff"));

        // Assert
        assertEquals(0, result.size());
    }
}
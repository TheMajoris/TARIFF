package com.cs203.core.service;

import com.cs203.core.dto.responses.NewsArticlesResponseDTO;

import java.util.List;

public interface NewsService {
    // fetch all latest news
    NewsArticlesResponseDTO fetchLatestNews();

    // fetch latest news by country
    NewsArticlesResponseDTO fetchLatestNewsByTags(List<String> tags);
}

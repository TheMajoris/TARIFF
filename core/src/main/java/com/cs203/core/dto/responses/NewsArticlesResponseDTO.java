package com.cs203.core.dto.responses;

import com.cs203.core.entity.NewsArticleEntity;

import java.util.List;

public record NewsArticlesResponseDTO(List<NewsArticleEntity> articles) {
}

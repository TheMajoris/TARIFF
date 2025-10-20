package com.cs203.core.controller;

import com.cs203.core.dto.responses.NewsArticlesResponseDTO;
import com.cs203.core.service.NewsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "News")
@RestController
@RequestMapping("/api/v1/news")
public class NewsController {
    private final NewsService newsService;

    @Autowired
    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @Operation(summary = "Get latest news")
    @ApiResponse(responseCode = "200", description = "Latest news successfuly fetched.")
    @GetMapping
    public ResponseEntity<NewsArticlesResponseDTO> getNews() {
        return ResponseEntity.ok(newsService.fetchLatestNews());
    }

    @Operation(summary = "Get news by tags")
    @ApiResponse(responseCode = "200", description = "Latest news received by tags.")
    @GetMapping(params = "tags")
    public ResponseEntity<NewsArticlesResponseDTO> getNewsByTags(
            @RequestParam
            @NotEmpty(message = "Tags list cannot be empty")
            @Size(max = 10, message = "Maximum 10 tags allowed")
            List<String> tags
    ) {
        return ResponseEntity.ok(newsService.fetchLatestNewsByTags(tags));
    }
}

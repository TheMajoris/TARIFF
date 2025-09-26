package com.cs203.core.controller;

import com.cs203.core.dto.responses.NewsArticlesResponseDTO;
import com.cs203.core.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/news")
public class NewsController {
    private final NewsService newsService;

    @Autowired
    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @GetMapping
    public ResponseEntity<NewsArticlesResponseDTO> getNews() {
        return ResponseEntity.ok(newsService.fetchLatestNews());
    }

    @GetMapping(params = "tags")
    public ResponseEntity<NewsArticlesResponseDTO> getNewsByTags(@RequestParam List<String> tags) {
        return ResponseEntity.ok(newsService.fetchLatestNewsByTags(tags));
    }

}

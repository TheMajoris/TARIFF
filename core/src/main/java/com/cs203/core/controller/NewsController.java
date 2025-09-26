package com.cs203.core.controller;

import com.cs203.core.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/news")
public class NewsController {
    private final NewsService newsService;

    @Autowired
    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @GetMapping("/scrape-news")
    public void scrapeNews() {
        newsService.scrapeNews();
    }

//     @GetMapping("/news")
//    public ResponseEntity<NewsArticlesResponseDTO> news() {
//    }
}

package com.cs203.core.service.impl;

import com.cs203.core.dto.responses.NewsArticlesResponseDTO;
import com.cs203.core.repository.NewsRepository;
import com.cs203.core.service.NewsService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class NewsServiceImpl implements NewsService {
    private final Logger logger = LoggerFactory.getLogger(NewsServiceImpl.class);

    private final NewsRepository newsRepository;

    @Autowired
    public NewsServiceImpl(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }


    @Override
    public void scrapeNews() {
        
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

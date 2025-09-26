package com.cs203.core.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "news")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class NewsArticleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "news_id")
    private Long id;

    @Column(nullable = false)
    String headline;

    @Column(nullable = false)
    String summary;

    @Column(nullable = false)
    String url;

    @Column(nullable = true)
    String tags;
}

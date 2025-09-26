package com.cs203.core.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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

    @Column(nullable = false, columnDefinition = "TEXT")
    String headline;

    @Column(nullable = false, columnDefinition = "TEXT")
    String summary;

    @Column(nullable = false, columnDefinition = "TEXT")
    String url;

    @ElementCollection
    @CollectionTable(name = "news_tags", joinColumns = @JoinColumn(name = "news_id"))
    @Column(name = "tag", nullable = true)
    List<String> tags = new ArrayList<>();
}

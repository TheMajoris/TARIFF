package com.cs203.core.repository;

import com.cs203.core.entity.NewsArticleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsRepository extends JpaRepository<NewsArticleEntity, Long> {

    List<NewsArticleEntity> findNewsArticleEntitiesById(Long id);

    List<NewsArticleEntity> findNewsArticleEntitiesByHeadline(String headline);

    @Query("SELECT DISTINCT n FROM NewsArticleEntity n JOIN n.tags t WHERE t IN :tags")
    List<NewsArticleEntity> findNewsArticleEntitiesByTags(@Param("tags") List<String> tags);
}

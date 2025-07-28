package com.vinsguru.playground.sec04;

import com.fasterxml.jackson.core.type.TypeReference;
import com.vinsguru.playground.AbstractTest;
import com.vinsguru.playground.sec03.entity.Product;
import com.vinsguru.playground.sec04.entity.Article;
import com.vinsguru.playground.sec04.repository.ArticleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.SearchHits;

import java.util.List;

public class QueryAnnotationTest extends AbstractTest {

    @Autowired
    private ArticleRepository repository;

    @BeforeAll
    public void setupData() {
        List<Article> articles = this.readResource("sec04/articles-data.json", new TypeReference<List<Article>>() {
        });
        repository.saveAll(articles);
        Assertions.assertEquals(11, repository.count());
    }

    @Test
    public void searchArticles() {
        this.print().accept("findByCategory");
        SearchHits<Article> searchHits = repository.search("spring seasen");
        searchHits.forEach(this.print());
        Assertions.assertEquals(4, searchHits.getTotalHits());
    }
}

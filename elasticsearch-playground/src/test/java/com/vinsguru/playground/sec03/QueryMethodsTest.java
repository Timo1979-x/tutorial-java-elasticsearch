package com.vinsguru.playground.sec03;

import com.fasterxml.jackson.core.type.TypeReference;
import com.vinsguru.playground.AbstractTest;
import com.vinsguru.playground.sec02.CrudOperationsTest;
import com.vinsguru.playground.sec03.entity.Product;
import com.vinsguru.playground.sec03.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.util.Streamable;

import java.util.List;

public class QueryMethodsTest extends AbstractTest {
    public static final Logger log = LoggerFactory.getLogger(QueryMethodsTest.class);

    @Autowired
    ProductRepository repository;

    @BeforeAll
    public void setupData() {
        List<Product> products = this.readResource("sec03/products.json", new TypeReference<List<Product>>() {
        });
        repository.saveAll(products);
        Assertions.assertEquals(20, repository.count());
    }

    @Test
    public void findByCategory() {
        this.print().accept("findByCategory");
        SearchHits<Product> searchHits = repository.findByCategory("Furniture");
        searchHits.forEach(this.print());
        Assertions.assertEquals(4, searchHits.getTotalHits());
    }

    @Test
    public void findByCategories() {
        this.print().accept("findByCategories");
        SearchHits<Product> searchHits = repository.findByCategoryIn(List.of("Furniture", "Beauty"));
        searchHits.forEach(this.print());
        Assertions.assertEquals(8, searchHits.getTotalHits());
    }

    @Test
    public void findByCategoryAndBrand() {
        this.print().accept("findByCategoryAndBrand");
        SearchHits<Product> searchHits = repository.findByCategoryAndBrand("Furniture", "Ikea");
        searchHits.forEach(this.print());
        Assertions.assertEquals(2, searchHits.getTotalHits());
    }

    @Test
    public void findByName() {
        this.print().accept("findByName");
        SearchHits<Product> searchHits = repository.findByName("table coffee");
        searchHits.forEach(this.print());
        Assertions.assertEquals(1, searchHits.getTotalHits());
    }

    @Test
    public void findByPriceLessThan() {
        this.print().accept("findByPriceLessThan");
        SearchHits<Product> searchHits = repository.findByPriceLessThan(80);
        searchHits.forEach(this.print());
        Assertions.assertEquals(5, searchHits.getTotalHits());
    }

    @Test
    public void findByPriceBetween() {
        this.print().accept("findByPriceBetween");
        SearchHits<Product> searchHits = repository.findByPriceBetween(10, 120, Sort.by("price"));
        searchHits.forEach(this.print());
        Assertions.assertEquals(8, searchHits.getTotalHits());
    }

    @Test
    public void findAllSortByQuantity() {
        this.print().accept("findAllSortByQuantity");
        var all = repository.findAll(Sort.by("quantity").descending());
        all.forEach(this.print());
        Assertions.assertEquals(20, Streamable.of(all).toList().size());
    }
}

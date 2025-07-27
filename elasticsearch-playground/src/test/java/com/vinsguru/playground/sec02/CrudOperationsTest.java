package com.vinsguru.playground.sec02;

import com.vinsguru.playground.AbstractTest;
import com.vinsguru.playground.sec01.entity.Customer;
import com.vinsguru.playground.sec01.entity.Movie;
import com.vinsguru.playground.sec01.entity.Review;
import com.vinsguru.playground.sec02.entity.Employee;
import com.vinsguru.playground.sec02.repository.EmployeeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.index.Settings;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;

public class CrudOperationsTest extends AbstractTest {
    public static final Logger log = LoggerFactory.getLogger(CrudOperationsTest.class);

    @Autowired
    private EmployeeRepository repository;

    @Test
    public void createIndex() {
    }

    @Test
    public void crud() {
        var employee = new Employee(1, "Sam", 34);

        // save
        repository.save(employee);
        printAll("After save");

        // find by id:
        employee = repository.findById(1).orElseThrow();
        Assertions.assertEquals(1, employee.getId());
        Assertions.assertEquals("Sam", employee.getName());
        Assertions.assertEquals(34, employee.getAge());

        // update and save:
        employee.setAge(32);
        employee = repository.save(employee);
        Assertions.assertEquals(1, employee.getId());
        Assertions.assertEquals("Sam", employee.getName());
        Assertions.assertEquals(32, employee.getAge());
        printAll("after update");

        // delete:
        repository.deleteById(1);
        Assertions.assertTrue(repository.findById(1).isEmpty());
        printAll("after delete");
    }

    private void printAll(String title) {
        log.info(title);
        repository.findAll().forEach(e -> log.info("employee: {}", e));
    }
}

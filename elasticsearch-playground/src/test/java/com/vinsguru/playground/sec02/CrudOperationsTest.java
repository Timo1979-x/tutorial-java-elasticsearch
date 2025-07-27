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
import org.springframework.data.util.Streamable;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

    @Test
    public void bulkCrud() {
        var employeeList = IntStream.rangeClosed(1, 10)
                .mapToObj(i -> new Employee(i, "name-" + i, i + 30))
                .toList();
        repository.saveAll(employeeList);
        printAll("After save");

        // check count:
        Assertions.assertEquals(10, repository.count());

        // find by ids:
        var ids = List.of(2, 4, 6);
        employeeList = Streamable.of(repository.findAllById(ids)).toList();
        Assertions.assertEquals(3, employeeList.size());

        // update and save
        employeeList.forEach(employee -> {employee.setAge(employee.getAge() + 10);});
        repository.saveAll(employeeList);
        printAll("After update");
        repository.findAllById(ids)
                .forEach(e -> Assertions.assertEquals(e.getId() + 40, e.getAge()));

        // delete and check count:
        repository.deleteAllById(ids);
        printAll("After delete");
        Assertions.assertEquals(7, repository.count());
    }

    private void printAll(String title) {
        log.info(title);
        repository.findAll().forEach(e -> log.info("employee: {}", e));
    }
}

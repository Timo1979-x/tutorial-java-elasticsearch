package com.vinsguru.playground.sec02.repository;

import com.vinsguru.playground.sec02.entity.Employee;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends ElasticsearchRepository<Employee, Integer> {

}

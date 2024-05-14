package com.kopacz.JAROSLAW_KOPACZ_TEST_5.processors;

import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.Employee;
import org.springframework.batch.item.ItemProcessor;

public class EmployeeProcessor implements ItemProcessor<Employee, Employee> {

    @Override
    public Employee process(Employee employee) throws Exception {
        return employee;
    }
}

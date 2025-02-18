package com.secretsanta.game.service;

import com.secretsanta.game.model.Employee;
import com.secretsanta.game.model.SecretSantaAssignment;
import com.secretsanta.game.services.SecretSantaService;
import com.secretsanta.game.services.SecretSantaServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@ExtendWith(MockitoExtension.class)
public class SecretSantaServiceTest {
    @InjectMocks
    SecretSantaServiceImpl secretSantaService;

    @Test
    public void testAssignSecretSanta() {
        List<Employee> employees = Arrays.asList(new Employee("emp1", "emp1@gmail.com"), new Employee("emp2", "emp2@gmail.com"));
        List<SecretSantaAssignment> result = secretSantaService.assignSecretSanta(employees, new ArrayList<>());
        assertEquals(2, result.size());
        assertEquals("emp1@gmail.com", result.get(0).getEmployee().getEmployeeEmail());
        assertEquals("emp2@gmail.com", result.get(0).getSecretChild().getEmployeeEmail());
        assertEquals("emp2@gmail.com", result.get(1).getEmployee().getEmployeeEmail());
        assertEquals("emp1@gmail.com", result.get(1).getSecretChild().getEmployeeEmail());

    }

    @Test
    public void testAssignSecretSantaWithPreviousYearAssignment() {
        Employee emp1 = new Employee("emp1", "emp1@gmail.com");
        Employee emp2 = new Employee("emp2", "emp2@gmail.com");
        Employee emp3 = new Employee("emp3", "emp3@gmail.com");
        Employee emp4 = new Employee("emp4", "emp4@gmail.com");
        List<Employee> employees = Arrays.asList(emp1, emp2, emp3, emp4);

        List<SecretSantaAssignment> prevYearAssign = Arrays.asList(new SecretSantaAssignment(emp1, emp2), new SecretSantaAssignment(emp3, emp4));
        List<SecretSantaAssignment> result = secretSantaService.assignSecretSanta(employees, prevYearAssign);
        assertEquals(4, result.size());
        assertEquals("emp1@gmail.com", result.get(0).getEmployee().getEmployeeEmail());
        assertTrue(!result.get(0).getSecretChild().getEmployeeEmail().equals("emp2@gmail.com"));
        assertEquals("emp3@gmail.com", result.get(2).getEmployee().getEmployeeEmail());
        assertTrue(!result.get(2).getSecretChild().getEmployeeEmail().equals("emp4@gmail.com"));
    }
}

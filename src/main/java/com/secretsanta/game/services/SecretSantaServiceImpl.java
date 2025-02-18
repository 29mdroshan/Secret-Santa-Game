package com.secretsanta.game.services;

import com.secretsanta.game.model.Employee;
import com.secretsanta.game.model.SecretSantaAssignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class SecretSantaServiceImpl implements SecretSantaService{
    @Override
    public List<SecretSantaAssignment> assignSecretSanta(List<Employee> employees, List<SecretSantaAssignment> previousAssignments) {
        List<Employee> availableEmployees = new ArrayList<>(employees);
        List<SecretSantaAssignment> assignmentsList = new ArrayList<>();

        for (Employee employee : employees) {
            Employee secretChild;
            boolean uniqueAssignment;
            do {
                uniqueAssignment = true;
                secretChild = availableEmployees.get(new Random().nextInt(availableEmployees.size()));

                if (secretChild.getEmployeeEmail().equals(employee.getEmployeeEmail())) {
                    uniqueAssignment = false;
                } else {
                    for (SecretSantaAssignment assignment : previousAssignments) {
                        if (assignment.getEmployee().getEmployeeEmail().equals(employee.getEmployeeEmail()) &&
                                assignment.getSecretChild().getEmployeeEmail().equals(secretChild.getEmployeeEmail())) {
                            uniqueAssignment = false;
                            break;
                        }
                    }
                }
            } while (!uniqueAssignment);
            assignmentsList.add(new SecretSantaAssignment(employee, secretChild));
            availableEmployees.remove(secretChild);
        }
        return assignmentsList;
    }
}

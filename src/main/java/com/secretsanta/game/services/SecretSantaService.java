package com.secretsanta.game.services;

import com.secretsanta.game.model.Employee;
import com.secretsanta.game.model.SecretSantaAssignment;

import java.util.List;

public interface SecretSantaService {

    List<SecretSantaAssignment> assignSecretSanta(List<Employee> employeeList, List<SecretSantaAssignment> previousAssignment);
}

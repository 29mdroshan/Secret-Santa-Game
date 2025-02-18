package com.secretsanta.game.controller;

import com.opencsv.exceptions.CsvValidationException;
import com.secretsanta.game.model.Employee;
import com.secretsanta.game.model.SecretSantaAssignment;
import com.secretsanta.game.services.CsvHelperService;
import com.secretsanta.game.services.SecretSantaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/v1")
public class SecretSantaController {
    @Autowired
    CsvHelperService csvHelperService;

    @Autowired
    SecretSantaService secretSantaService;

    @PostMapping("/assign/secret/santa")
    public ResponseEntity<Resource> assignSecretSanta(
            @RequestParam("employees") MultipartFile employeesFile,
            @RequestParam(value = "previousAssignments", required = false) MultipartFile previousAssignmentsFile) {

        List<Employee> employeeList = csvHelperService.parseEmployeeCsv(employeesFile);
        List<SecretSantaAssignment> previousAssignmentsList = new ArrayList<>();

        if (previousAssignmentsFile != null && !previousAssignmentsFile.isEmpty()) {
            previousAssignmentsList = csvHelperService.readPreviousAssignments(previousAssignmentsFile);
        }

        List<SecretSantaAssignment> resultAssignments = secretSantaService.assignSecretSanta(employeeList, previousAssignmentsList);
        File resultFile = csvHelperService.writeCSV(resultAssignments);

        Resource resource = new FileSystemResource(resultFile);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resultFile.getName() + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

}

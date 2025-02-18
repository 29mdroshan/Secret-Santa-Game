package com.secretsanta.game.services;

import com.opencsv.exceptions.CsvValidationException;
import com.secretsanta.game.model.Employee;
import com.secretsanta.game.model.SecretSantaAssignment;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface CsvHelperService {

    List<Employee> parseEmployeeCsv(MultipartFile file);
    List<SecretSantaAssignment> readPreviousAssignments(MultipartFile filePath);

    File writeCSV(List<SecretSantaAssignment> ssAssignmentList);
}

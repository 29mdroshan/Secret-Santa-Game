package com.secretsanta.game.services;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import com.secretsanta.game.exception.CsvProcessingException;
import com.secretsanta.game.model.Employee;
import com.secretsanta.game.model.SecretSantaAssignment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

@Service
public class CsvHelperServiceImpl implements CsvHelperService {

    @Override
    public List<Employee> parseEmployeeCsv(MultipartFile file) {
        try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
            List<Employee> employeeList = new ArrayList<>();
            String[] header = reader.readNext();
            if (header == null || header.length < 2 || !header[0].equalsIgnoreCase("Employee_Name") || !header[1].equalsIgnoreCase("Employee_EmailID")) {
                throw new CsvProcessingException("Invalid CSV header. The first column must be Employee_EmailID and second column must be Employee_EmailID");
            }
            String[] nextLine;
            HashSet<String> emailSet = new HashSet<>();
            while ((nextLine = reader.readNext()) != null) {
                if (nextLine.length < 2 || nextLine[0] == null || nextLine[0].isEmpty() || nextLine[1] == null || nextLine[1].isEmpty()) {
                    throw new CsvProcessingException("Invalid CSV format. Each row must contain a non-empty name and email.");
                }
                String employeeName = nextLine[0];
                String employeeEmail = nextLine[1];
                if (!emailSet.add(employeeEmail)) {
                    throw new CsvProcessingException("Duplicate employee email Ids found: " + employeeEmail);
                }
                employeeList.add(new Employee(employeeName, employeeEmail));
            }
            return employeeList;
        } catch (IOException | CsvValidationException exception) {
            throw new CsvProcessingException("Error reading Employee csv file", exception);
        }
    }

    public  List<SecretSantaAssignment> readPreviousAssignments(MultipartFile file) {
        try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
            List<SecretSantaAssignment> previousAssignments = new ArrayList<>();

            String[] header = reader.readNext();
            if (header == null || header.length < 4 || !header[0].equalsIgnoreCase("Employee_Name") || !header[1].equalsIgnoreCase("Employee_EmailID") ||
                    !header[2].equalsIgnoreCase("Secret_Child_Name") || !header[3].equalsIgnoreCase("Secret_Child_EmailID")) {
                throw new CsvProcessingException("Invalid CSV header. The first column must be Employee_EmailID, second column must be Employee_EmailID, third column must be Secret_Child_Name, fourth column must be Secret_Child_EmailID");
            }

            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                if (nextLine.length < 4 || nextLine[0] == null || nextLine[0].isEmpty() || nextLine[1] == null || nextLine[1].isEmpty() || nextLine[2] == null || nextLine[2].isEmpty() || nextLine[3] == null || nextLine[3].isEmpty()) {
                    throw new CsvProcessingException("Invalid CSV format. Each row must contain non-empty employee and secret child information.");
                }
                Employee employee = new Employee(nextLine[0], nextLine[1]);
                Employee secretChild = new Employee(nextLine[2], nextLine[3]);

                previousAssignments.add(new SecretSantaAssignment(employee, secretChild));
            }
            return previousAssignments;
        } catch (IOException | CsvValidationException exception) {
            throw new CsvProcessingException("Error reading previous assignments CSV file", exception);
        }
    }

    @Override
    public File writeCSV(List<SecretSantaAssignment> assignments) {
        String fileName = "results_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".csv";
        File resultFile = new File(fileName);
        try (CSVWriter writer = new CSVWriter(new FileWriter(resultFile))) {
            String[] header = {"Employee_Name", "Employee_EmailID", "Secret_Child_Name", "Secret_Child_EmailID"};
            writer.writeNext(header);
            for (SecretSantaAssignment assignment : assignments) {
                writer.writeNext(new String[]{
                        assignment.getEmployee().getEmployeeName(),
                        assignment.getEmployee().getEmployeeEmail(),
                        assignment.getSecretChild().getEmployeeName(),
                        assignment.getSecretChild().getEmployeeEmail()
                });
            }
        } catch (IOException exception) {
            throw new RuntimeException("Error in writing the CSV file", exception);
        }
        return resultFile;
    }
}

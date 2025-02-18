package com.secretsanta.game.service;

import com.secretsanta.game.exception.CsvProcessingException;
import com.secretsanta.game.model.Employee;
import com.secretsanta.game.model.SecretSantaAssignment;
import com.secretsanta.game.services.CsvHelperServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

@ExtendWith(MockitoExtension.class)
public class CsvHelperServiceTest {


    @InjectMocks
    private CsvHelperServiceImpl csvHelperService;

    @Test
    public void testParseEmployeeCsv() throws Exception {
        String csvContent = "Employee_Name,Employee_EmailID\nemp1,emp1@example.com\nemp2,emp2@example.com";
        MockMultipartFile file = new MockMultipartFile("file", "employees.csv", "text/csv", csvContent.getBytes());

        List<Employee> result = csvHelperService.parseEmployeeCsv(file);

        assertEquals(2, result.size());
        assertEquals("emp1", result.get(0).getEmployeeName());
        assertEquals("emp1@example.com", result.get(0).getEmployeeEmail());
        assertEquals("emp2", result.get(1).getEmployeeName());
        assertEquals("emp2@example.com", result.get(1).getEmployeeEmail());
    }

    @Test
    public void testParseEmployeeCsvWithInvalidHeader() throws Exception {
        String csvContent = "Name,Email\nemp1,emp1@example.com";
        MockMultipartFile file = new MockMultipartFile("file", "employees.csv", "text/csv", csvContent.getBytes());

        CsvProcessingException exception = assertThrows(CsvProcessingException.class, () -> {
            csvHelperService.parseEmployeeCsv(file);
        });

        assertEquals("Invalid CSV header. The first column must be Employee_EmailID and second column must be Employee_EmailID", exception.getMessage());
    }

    @Test
    public void testParseEmployeeCsvWithDuplicateEmail() throws Exception {
        String csvContent = "Employee_Name,Employee_EmailID\nemp1,emp1@example.com\nemp2,emp1@example.com";
        MockMultipartFile file = new MockMultipartFile("file", "employees.csv", "text/csv", csvContent.getBytes());

        CsvProcessingException exception = assertThrows(CsvProcessingException.class, () -> {
            csvHelperService.parseEmployeeCsv(file);
        });

        assertEquals("Duplicate employee email Ids found: emp1@example.com", exception.getMessage());
    }

    @Test
    public void testReadPreviousAssignments() throws Exception {
        String csvContent = "Employee_Name,Employee_EmailID,Secret_Child_Name,Secret_Child_EmailID\nemp1,emp1@example.com,emp2,emp2@example.com";
        MockMultipartFile file = new MockMultipartFile("file", "previous_assignments.csv", "text/csv", csvContent.getBytes());

        List<SecretSantaAssignment> result = csvHelperService.readPreviousAssignments(file);

        assertEquals(1, result.size());
        assertEquals("emp1", result.get(0).getEmployee().getEmployeeName());
        assertEquals("emp2", result.get(0).getSecretChild().getEmployeeName());
    }


    @Test
    public void testReadPreviousAssignments_InvalidHeader() throws Exception {
        String csvContent = "Name,Email,Secret_Name,Secret_Email\nemp1,emp1@example.com,emp2,emp2@example.com";
        MockMultipartFile file = new MockMultipartFile("file", "previous_assignments.csv", "text/csv", csvContent.getBytes());
        CsvProcessingException exception = assertThrows(CsvProcessingException.class, () -> {
            csvHelperService.readPreviousAssignments(file);
        });
        assertEquals("Invalid CSV header. The first column must be Employee_EmailID, second column must be Employee_EmailID, third column must be Secret_Child_Name, fourth column must be Secret_Child_EmailID", exception.getMessage());
    }


    @Test
    public void testWriteCSV() throws IOException {
        List<SecretSantaAssignment> assignments = Arrays.asList(
                new SecretSantaAssignment(new Employee("emp1", "emp1@example.com"), new Employee("emp2", "emp2@example.com"))
        );
        File resultFile = csvHelperService.writeCSV(assignments);
        assertTrue(resultFile.exists());
        assertTrue(resultFile.getName().startsWith("results_"));
        assertTrue(resultFile.getName().endsWith(".csv"));
    }
}

package com.secretsanta.game.controller;

import com.secretsanta.game.model.Employee;
import com.secretsanta.game.model.SecretSantaAssignment;
import com.secretsanta.game.services.CsvHelperService;
import com.secretsanta.game.services.SecretSantaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@WebMvcTest(SecretSantaController.class)
public class SecretSantaControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CsvHelperService csvHelperService;

    @MockBean
    private SecretSantaService secretSantaService;

    @Test
    public void testAssignSecretSanta() throws Exception {
        MockMultipartFile employeesFile = new MockMultipartFile("employees",
                "employees.csv", MediaType.TEXT_PLAIN_VALUE,
                "Employee_Name,Employee_EmailID\nemp1,emp1@example.com\nemp2,emp2@example.com".getBytes());

        MockMultipartFile previousAssignmentsFile = new MockMultipartFile("previousAssignments",
                "previousAssignments.csv", MediaType.TEXT_PLAIN_VALUE,
                "".getBytes());

        List<Employee> employeeList = new ArrayList<>();
        employeeList.add(new Employee("emp1", "emp1@example.com"));
        employeeList.add(new Employee("emp2", "emp2@example.com"));

        List<SecretSantaAssignment> assignmentList = new ArrayList<>();
        assignmentList.add(new SecretSantaAssignment(
                new Employee("emp1", "emp1@example.com"),
                new Employee("emp2", "emp2@example.com")
        ));

        when(csvHelperService.parseEmployeeCsv(any(MultipartFile.class))).thenReturn(employeeList);
        when(csvHelperService.readPreviousAssignments(any(MultipartFile.class))).thenReturn(new ArrayList<>());
        when(secretSantaService.assignSecretSanta(anyList(), anyList())).thenReturn(assignmentList);

        File tempFile = File.createTempFile("result", ".csv");
        tempFile.deleteOnExit();

        when(csvHelperService.writeCSV(anyList())).thenReturn(tempFile);

        mockMvc.perform(MockMvcRequestBuilders.multipart("/v1/assign/secret/santa")
                        .file(employeesFile)
                        .file(previousAssignmentsFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + tempFile.getName() + "\""));
    }

    @Test
    public void testAssignSecretSantaWithException() throws Exception {
        MockMultipartFile employeesFile = new MockMultipartFile("employees",
                "employees.csv", MediaType.TEXT_PLAIN_VALUE,
                "".getBytes());

        MockMultipartFile previousAssignmentsFile = new MockMultipartFile("previousAssignments",
                "previousAssignments.csv", MediaType.TEXT_PLAIN_VALUE,
                "".getBytes());

        when(csvHelperService.parseEmployeeCsv(any(MultipartFile.class))).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/v2/assign/secret/santa")
                        .file(employeesFile)
                        .file(previousAssignmentsFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

}

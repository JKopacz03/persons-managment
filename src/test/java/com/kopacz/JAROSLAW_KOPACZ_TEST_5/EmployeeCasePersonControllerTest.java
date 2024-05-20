package com.kopacz.JAROSLAW_KOPACZ_TEST_5;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.config.ClearContext;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.config.DatabaseUtils;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.config.PersonsTest;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.User;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.UserRole;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.command.*;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.command.edit.EmployeeEditCommand;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.service.JwtService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.time.LocalDate;

import static java.lang.String.format;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@PersonsTest
@ActiveProfiles("testH2")
@SpringBootTest
@Testcontainers
public class EmployeeCasePersonControllerTest {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final JwtService jwtService;
    private final DatabaseUtils databaseUtils;
    @Autowired
    public EmployeeCasePersonControllerTest(MockMvc mockMvc, ObjectMapper objectMapper,
                                            JwtService jwtService, DatabaseUtils databaseUtils) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.jwtService = jwtService;
        this.databaseUtils = databaseUtils;
    }

    @Test
    void shouldReturnsEmployeesWorkDateFrom20220309() throws Exception {

        mockMvc.perform(get("/person/find?type=employee&workStartDateFrom=2022-03-09"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("Emma"))
                .andExpect(jsonPath("$[0].lastName").value("Johnson"))
                .andExpect(jsonPath("$[0].peselNumber").value("83040445678"))
                .andExpect(jsonPath("$[0].height").value(170.0))
                .andExpect(jsonPath("$[0].weight").value(65.0))
                .andExpect(jsonPath("$[0].email").value("emmajohnson@example.com"))
                .andExpect(jsonPath("$[0].workStartDate").value("2022-04-01"))
                .andExpect(jsonPath("$[0].actualProfession").value("Analyst"))
                .andExpect(jsonPath("$[0].salary").value(5500.00))
                .andExpect(jsonPath("$[0].numberOfPositions").value(0))
                .andExpect(jsonPath("$[1].firstName").value("Michael"))
                .andExpect(jsonPath("$[1].lastName").value("Williams"))
                .andExpect(jsonPath("$[1].peselNumber").value("84050556789"))
                .andExpect(jsonPath("$[1].height").value(185.0))
                .andExpect(jsonPath("$[1].weight").value(90.0))
                .andExpect(jsonPath("$[1].email").value("michaelwilliams@example.com"))
                .andExpect(jsonPath("$[1].workStartDate").value("2022-05-01"))
                .andExpect(jsonPath("$[1].actualProfession").value("Engineer"))
                .andExpect(jsonPath("$[1].salary").value(7000.00))
                .andExpect(jsonPath("$[1].numberOfPositions").value(0));

    }

    @Test
    void shouldReturnsEmployeesWorkDateTo20220209() throws Exception {

        mockMvc.perform(get("/person/find?type=employee&workStartDateTo=2022-02-09"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].lastName").value("Doe"))
                .andExpect(jsonPath("$[0].peselNumber").value("80010112345"))
                .andExpect(jsonPath("$[0].height").value(175.0))
                .andExpect(jsonPath("$[0].weight").value(80.0))
                .andExpect(jsonPath("$[0].email").value("johndoe@example.com"))
                .andExpect(jsonPath("$[0].workStartDate").value("2022-01-01"))
                .andExpect(jsonPath("$[0].actualProfession").value("Programmer"))
                .andExpect(jsonPath("$[0].salary").value(5000.00))
                .andExpect(jsonPath("$[0].numberOfPositions").value(2))
                .andExpect(jsonPath("$[1].firstName").value("Alice"))
                .andExpect(jsonPath("$[1].lastName").value("Smith"))
                .andExpect(jsonPath("$[1].peselNumber").value("81020223456"))
                .andExpect(jsonPath("$[1].height").value(165.0))
                .andExpect(jsonPath("$[1].weight").value(60.0))
                .andExpect(jsonPath("$[1].email").value("alicesmith@example.com"))
                .andExpect(jsonPath("$[1].workStartDate").value("2022-02-01"))
                .andExpect(jsonPath("$[1].actualProfession").value("Designer"))
                .andExpect(jsonPath("$[1].salary").value(4500.00))
                .andExpect(jsonPath("$[1].numberOfPositions").value(1));
    }

    @Test
    void shouldReturnsEmployeesActualProfessionDesigner() throws Exception {

        mockMvc.perform(get("/person/find?type=employee&actualProfession=designer"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("Alice"))
                .andExpect(jsonPath("$[0].lastName").value("Smith"))
                .andExpect(jsonPath("$[0].peselNumber").value("81020223456"))
                .andExpect(jsonPath("$[0].height").value(165.0))
                .andExpect(jsonPath("$[0].weight").value(60.0))
                .andExpect(jsonPath("$[0].email").value("alicesmith@example.com"))
                .andExpect(jsonPath("$[0].workStartDate").value("2022-02-01"))
                .andExpect(jsonPath("$[0].actualProfession").value("Designer"))
                .andExpect(jsonPath("$[0].salary").value(4500.00))
                .andExpect(jsonPath("$[0].numberOfPositions").value(1));
    }

    @Test
    void shouldReturnsEmployeesSalaryFrom7000() throws Exception {

        mockMvc.perform(get("/person/find?type=employee&salaryFrom=7000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("Michael"))
                .andExpect(jsonPath("$[0].lastName").value("Williams"))
                .andExpect(jsonPath("$[0].peselNumber").value("84050556789"))
                .andExpect(jsonPath("$[0].height").value(185.0))
                .andExpect(jsonPath("$[0].weight").value(90.0))
                .andExpect(jsonPath("$[0].email").value("michaelwilliams@example.com"))
                .andExpect(jsonPath("$[0].workStartDate").value("2022-05-01"))
                .andExpect(jsonPath("$[0].actualProfession").value("Engineer"))
                .andExpect(jsonPath("$[0].salary").value(7000.00))
                .andExpect(jsonPath("$[0].numberOfPositions").value(0));
    }

    @Test
    void shouldReturnsEmployeesSalaryTo5000() throws Exception {

        mockMvc.perform(get("/person/find?type=employee&salaryTo=5000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].lastName").value("Doe"))
                .andExpect(jsonPath("$[0].peselNumber").value("80010112345"))
                .andExpect(jsonPath("$[0].height").value(175.0))
                .andExpect(jsonPath("$[0].weight").value(80.0))
                .andExpect(jsonPath("$[0].email").value("johndoe@example.com"))
                .andExpect(jsonPath("$[0].workStartDate").value("2022-01-01"))
                .andExpect(jsonPath("$[0].actualProfession").value("Programmer"))
                .andExpect(jsonPath("$[0].salary").value(5000.00))
                .andExpect(jsonPath("$[0].numberOfPositions").value(2))
                .andExpect(jsonPath("$[1].firstName").value("Alice"))
                .andExpect(jsonPath("$[1].lastName").value("Smith"))
                .andExpect(jsonPath("$[1].peselNumber").value("81020223456"))
                .andExpect(jsonPath("$[1].height").value(165.0))
                .andExpect(jsonPath("$[1].weight").value(60.0))
                .andExpect(jsonPath("$[1].email").value("alicesmith@example.com"))
                .andExpect(jsonPath("$[1].workStartDate").value("2022-02-01"))
                .andExpect(jsonPath("$[1].actualProfession").value("Designer"))
                .andExpect(jsonPath("$[1].salary").value(4500.00))
                .andExpect(jsonPath("$[1].numberOfPositions").value(1));
    }

    @Test
    void shouldReturnsEmployeesNumberOfPositionsFrom2() throws Exception {

        mockMvc.perform(get("/person/find?type=employee&numberOfPositionsFrom=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].lastName").value("Doe"))
                .andExpect(jsonPath("$[0].peselNumber").value("80010112345"))
                .andExpect(jsonPath("$[0].height").value(175.0))
                .andExpect(jsonPath("$[0].weight").value(80.0))
                .andExpect(jsonPath("$[0].email").value("johndoe@example.com"))
                .andExpect(jsonPath("$[0].employeeId").value("11"))
                .andExpect(jsonPath("$[0].workStartDate").value("2022-01-01"))
                .andExpect(jsonPath("$[0].actualProfession").value("Programmer"))
                .andExpect(jsonPath("$[0].salary").value(5000.00))
                .andExpect(jsonPath("$[0].numberOfPositions").value(2));
    }

    @Test
    void shouldReturnsEmployeesNumberOfPositionsTo1() throws Exception {

        mockMvc.perform(get("/person/find?type=employee&numberOfPositionsTo=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("Alice"))
                .andExpect(jsonPath("$[0].lastName").value("Smith"))
                .andExpect(jsonPath("$[0].peselNumber").value("81020223456"))
                .andExpect(jsonPath("$[0].height").value(165.0))
                .andExpect(jsonPath("$[0].weight").value(60.0))
                .andExpect(jsonPath("$[0].email").value("alicesmith@example.com"))
                .andExpect(jsonPath("$[0].employeeId").value("12"))
                .andExpect(jsonPath("$[0].workStartDate").value("2022-02-01"))
                .andExpect(jsonPath("$[0].actualProfession").value("Designer"))
                .andExpect(jsonPath("$[0].salary").value(4500.00))
                .andExpect(jsonPath("$[0].numberOfPositions").value(1))
                .andExpect(jsonPath("$[1].firstName").value("James"))
                .andExpect(jsonPath("$[1].lastName").value("Brown"))
                .andExpect(jsonPath("$[1].peselNumber").value("82030334567"))
                .andExpect(jsonPath("$[1].height").value(180.0))
                .andExpect(jsonPath("$[1].weight").value(75.0))
                .andExpect(jsonPath("$[1].email").value("jamesbrown@example.com"))
                .andExpect(jsonPath("$[1].employeeId").value("13"))
                .andExpect(jsonPath("$[1].workStartDate").value("2022-03-01"))
                .andExpect(jsonPath("$[1].actualProfession").value("Manager"))
                .andExpect(jsonPath("$[1].salary").value(6000.00))
                .andExpect(jsonPath("$[1].numberOfPositions").value(0))
                .andExpect(jsonPath("$[2].firstName").value("Emma"))
                .andExpect(jsonPath("$[2].lastName").value("Johnson"))
                .andExpect(jsonPath("$[2].peselNumber").value("83040445678"))
                .andExpect(jsonPath("$[2].height").value(170.0))
                .andExpect(jsonPath("$[2].weight").value(65.0))
                .andExpect(jsonPath("$[2].email").value("emmajohnson@example.com"))
                .andExpect(jsonPath("$[2].employeeId").value("14"))
                .andExpect(jsonPath("$[2].workStartDate").value("2022-04-01"))
                .andExpect(jsonPath("$[2].actualProfession").value("Analyst"))
                .andExpect(jsonPath("$[2].salary").value(5500.00))
                .andExpect(jsonPath("$[2].numberOfPositions").value(0))
                .andExpect(jsonPath("$[3].firstName").value("Michael"))
                .andExpect(jsonPath("$[3].lastName").value("Williams"))
                .andExpect(jsonPath("$[3].peselNumber").value("84050556789"))
                .andExpect(jsonPath("$[3].height").value(185.0))
                .andExpect(jsonPath("$[3].weight").value(90.0))
                .andExpect(jsonPath("$[3].email").value("michaelwilliams@example.com"))
                .andExpect(jsonPath("$[3].employeeId").value("15"))
                .andExpect(jsonPath("$[3].workStartDate").value("2022-05-01"))
                .andExpect(jsonPath("$[3].actualProfession").value("Engineer"))
                .andExpect(jsonPath("$[3].salary").value(7000.00))
                .andExpect(jsonPath("$[3].numberOfPositions").value(0));
    }
    @Test
    @ClearContext
    void shouldAddEmployee() throws Exception {
        User user = new User(null, "admin", "qwerty", UserRole.ADMIN);
        String token = jwtService.generateToken(user);

        EmployeeCommand employeeCommand = new EmployeeCommand(
                "Jan",
                "Kowalski",
                "81981299",
                180.0,
                90.0,
                "janekkowal@gmail.com",
                LocalDate.of(2022, 12,22),
                "designer",
                BigDecimal.valueOf(3000)
        );
        String json = objectMapper.writeValueAsString(employeeCommand);

        mockMvc.perform(post("/person")
                        .header("Authorization", format("Bearer %s", token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("Jan"))
                .andExpect(jsonPath("$.lastName").value("Kowalski"))
                .andExpect(jsonPath("$.peselNumber").value("81981299"))
                .andExpect(jsonPath("$.height").value(180.0))
                .andExpect(jsonPath("$.weight").value(90.0))
                .andExpect(jsonPath("$.email").value("janekkowal@gmail.com"))
                .andExpect(jsonPath("$.workStartDate").value("2022-12-22"))
                .andExpect(jsonPath("$.actualProfession").value("designer"))
                .andExpect(jsonPath("$.salary").value(3000.0))
                .andExpect(jsonPath("$.numberOfPositions").value(0));
    }


    @Test
    void missingWorkStartDate_shouldReturnBadRequest() throws Exception {
        User user = new User(null, "admin", "qwerty", UserRole.ADMIN);
        String token = jwtService.generateToken(user);

        EmployeeCommand employeeCommand = new EmployeeCommand(
                "Jan",
                "Kowalski",
                "81981299",
                180.0,
                90.0,
                "janekkowal@gmail.com",
                null,
                "designer",
                BigDecimal.valueOf(3000)
        );
        String json = objectMapper.writeValueAsString(employeeCommand);

        mockMvc.perform(post("/person")
                        .header("Authorization", format("Bearer %s", token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("invalid date"));
    }

    @Test
    void missingActualProfession_shouldReturnBadRequest() throws Exception {
        User user = new User(null, "admin", "qwerty", UserRole.ADMIN);
        String token = jwtService.generateToken(user);

        EmployeeCommand employeeCommand = new EmployeeCommand(
                "Jan",
                "Kowalski",
                "81981299",
                180.0,
                90.0,
                "janekkowal@gmail.com",
                LocalDate.of(2022,12,22),
                "",
                BigDecimal.valueOf(3000)
        );
        String json = objectMapper.writeValueAsString(employeeCommand);

        mockMvc.perform(post("/person")
                        .header("Authorization", format("Bearer %s", token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("actualProfession is mandatory"));
    }

    @Test
    void missingSalary_shouldReturnBadRequest() throws Exception {
        User user = new User(null, "admin", "qwerty", UserRole.ADMIN);
        String token = jwtService.generateToken(user);

        EmployeeCommand employeeCommand = new EmployeeCommand(
                "Jan",
                "Kowalski",
                "81981299",
                180.0,
                90.0,
                "janekkowal@gmail.com",
                LocalDate.of(2022,12,22),
                "designer",
                BigDecimal.valueOf(0)
        );
        String json = objectMapper.writeValueAsString(employeeCommand);

        mockMvc.perform(post("/person")
                        .header("Authorization", format("Bearer %s", token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("missing salary"));
    }

    @Test
    @ClearContext
    void shouldEditEmployeeEmailAndActualProfession() throws Exception {
        User user = new User(null, "admin", "qwerty", UserRole.ADMIN);
        String token = jwtService.generateToken(user);

        EmployeeEditCommand employeeEditCommandCommand = new EmployeeEditCommand(
                12L,
                "Alice",
                "Smith",
                "81020223456",
                165.0,
                60.0,
                "alicesmith2@example.com",
                1,
                LocalDate.of(2022,2,1),
                "Call center",
                BigDecimal.valueOf(4500)
        );
        String json = objectMapper.writeValueAsString(employeeEditCommandCommand);

        mockMvc.perform(put("/person/12")
                        .header("Authorization", format("Bearer %s", token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        mockMvc.perform(get("/person/find?type=employee&peselNumber=81020223456"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("Alice"))
                .andExpect(jsonPath("$[0].lastName").value("Smith"))
                .andExpect(jsonPath("$[0].peselNumber").value("81020223456"))
                .andExpect(jsonPath("$[0].height").value(165.0))
                .andExpect(jsonPath("$[0].weight").value(60.0))
                .andExpect(jsonPath("$[0].email").value("alicesmith2@example.com"))
                .andExpect(jsonPath("$[0].workStartDate").value("2022-02-01"))
                .andExpect(jsonPath("$[0].actualProfession").value("Call center"))
                .andExpect(jsonPath("$[0].salary").value(4500.00))
                .andExpect(jsonPath("$[0].numberOfPositions").value(1));
    }

    @Test
    @ClearContext
    public void shouldImport100kEmployeesUnder3s() throws Exception {
        User user = new User(null, "importer", "qwerty", UserRole.IMPORTER);
        String token = jwtService.generateToken(user);

        ClassPathResource resource = new ClassPathResource("test/testingCsv/employees.csv");

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "employees.csv",
                "csv",
                resource.getInputStream());

        mockMvc.perform(multipart("/person/import?type=employee").file(file)
                        .header("Authorization", format("Bearer %s", token)))
                .andExpect(status().isAccepted());

        Thread.sleep(3500);

        if (databaseUtils.countRecordsInDatabase() != 100015) {
            Assertions.fail("Missing imports");
        }
    }

    @Test
    @ClearContext
    public void shouldRollbackAllInserts() throws Exception {
        User user = new User(null, "importer", "qwerty", UserRole.IMPORTER);
        String token = jwtService.generateToken(user);

        ClassPathResource resource = new ClassPathResource("test/testingCsv/invalidEmployees.csv");

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "invalidEmployees.csv",
                "csv",
                resource.getInputStream());

        mockMvc.perform(multipart("/person/import?type=employee").file(file)
                        .header("Authorization", format("Bearer %s", token)))
                .andExpect(status().isAccepted());

        if (databaseUtils.countRecordsInDatabase() != 15) {
            Assertions.fail("Failed rollback");
        }
    }


}


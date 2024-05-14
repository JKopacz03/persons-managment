package com.kopacz.JAROSLAW_KOPACZ_TEST_5;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.config.ClearContext;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.config.DatabaseUtils;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.config.PersonsTest;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.User;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.UserRole;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.command.PositionCommand;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.command.find.EmployeeFindCommand;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@PersonsTest
@Testcontainers
@ActiveProfiles("test")
@SpringBootTest
public class EmployeeControllerTest {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final JwtService jwtService;
    private final DatabaseUtils databaseUtils;
    @Container
    private static PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("postgres:15-alpine3.18")
                    .withDatabaseName("exchange")
                    .withPassword("qwerty")
                    .withUsername("postgres");

    @DynamicPropertySource
    public static void containerConfig(DynamicPropertyRegistry registry){
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @Autowired
    public EmployeeControllerTest(MockMvc mockMvc, ObjectMapper objectMapper,
                                  JwtService jwtService, DatabaseUtils databaseUtils) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.jwtService = jwtService;
        this.databaseUtils = databaseUtils;
    }

    @Test
    @ClearContext
    public void shouldAddPosition() throws Exception {
        User user = new User(null, "employee", "qwerty", UserRole.EMPLOYEE);
        String token = jwtService.generateToken(user);

        PositionCommand positionCommand = new PositionCommand(
                "Soviet designer",
                LocalDate.of(2022,12,22),
                LocalDate.of(2023,12,2),
                BigDecimal.valueOf(3000));

        String json = objectMapper.writeValueAsString(positionCommand);

        mockMvc.perform(post("/employee/80010112345")
                        .header("Authorization", format("Bearer %s", token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        EmployeeFindCommand employeeCommand = new EmployeeFindCommand(
                null,
                null,
                "80010112345",
                0,
                0,
                0,
                0,
                null,
                null,
                null,
                null,
                null,
                null,
                0,
                0
        );
        String json2 = objectMapper.writeValueAsString(employeeCommand);

        mockMvc.perform(get("/person/find")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json2))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].numberOfPositions").value(1));
    }

    @Test
    @ClearContext
    public void collideDates_shouldReturnBadRequest() throws Exception {
        User user = new User(null, "employee", "qwerty", UserRole.EMPLOYEE);
        String token = jwtService.generateToken(user);

        PositionCommand positionCommand = new PositionCommand(
                "Soviet designer",
                LocalDate.of(2022,12,22),
                LocalDate.of(2023,12,2),
                BigDecimal.valueOf(3000));

        PositionCommand positionCommand2 = new PositionCommand(
                "Soviet designer",
                LocalDate.of(2023,2,2),
                LocalDate.of(2025,12,2),
                BigDecimal.valueOf(3000));

        String json = objectMapper.writeValueAsString(positionCommand);
        String json2 = objectMapper.writeValueAsString(positionCommand2);

        mockMvc.perform(post("/employee/80010112345")
                        .header("Authorization", format("Bearer %s", token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        mockMvc.perform(post("/employee/80010112345")
                        .header("Authorization", format("Bearer %s", token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json2))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("your dates are collide with another positions"));
    }

    @Test
    public void invalidDates_shouldReturnBadRequest() throws Exception {
        User user = new User(null, "employee", "qwerty", UserRole.EMPLOYEE);
        String token = jwtService.generateToken(user);

        PositionCommand positionCommand = new PositionCommand(
                "Soviet designer",
                LocalDate.of(2022,12,22),
                LocalDate.of(2022,11,2),
                BigDecimal.valueOf(3000));

        String json = objectMapper.writeValueAsString(positionCommand);

        mockMvc.perform(post("/employee/80010112345")
                        .header("Authorization", format("Bearer %s", token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("end date must be after start date!"));
    }

    @Test
    public void invalidName_shouldReturnBadRequest() throws Exception {
        User user = new User(null, "employee", "qwerty", UserRole.EMPLOYEE);
        String token = jwtService.generateToken(user);

        PositionCommand positionCommand = new PositionCommand(
                "",
                LocalDate.of(2022,12,22),
                LocalDate.of(2023,11,2),
                BigDecimal.valueOf(3000));

        String json = objectMapper.writeValueAsString(positionCommand);

        mockMvc.perform(post("/employee/80010112345")
                        .header("Authorization", format("Bearer %s", token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("name is mandatory"));
    }

    @Test
    public void invalidStartDate_shouldReturnBadRequest() throws Exception {
        User user = new User(null, "employee", "qwerty", UserRole.EMPLOYEE);
        String token = jwtService.generateToken(user);

        PositionCommand positionCommand = new PositionCommand(
                "soviet programmer",
                null,
                LocalDate.of(2023,11,2),
                BigDecimal.valueOf(3000));

        String json = objectMapper.writeValueAsString(positionCommand);

        mockMvc.perform(post("/employee/80010112345")
                        .header("Authorization", format("Bearer %s", token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("startDate is mandatory"));
    }


    @Test
    public void invalidEndDate_shouldReturnBadRequest() throws Exception {
        User user = new User(null, "employee", "qwerty", UserRole.EMPLOYEE);
        String token = jwtService.generateToken(user);

        PositionCommand positionCommand = new PositionCommand(
                "soviet programmer",
                LocalDate.of(2023,11,2),
                null,
                BigDecimal.valueOf(3000));

        String json = objectMapper.writeValueAsString(positionCommand);

        mockMvc.perform(post("/employee/80010112345")
                        .header("Authorization", format("Bearer %s", token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("endDate is mandatory"));
    }

    @Test
    public void invalidSalary_shouldReturnBadRequest() throws Exception {
        User user = new User(null, "employee", "qwerty", UserRole.EMPLOYEE);
        String token = jwtService.generateToken(user);

        PositionCommand positionCommand = new PositionCommand(
                "soviet programmer",
                LocalDate.of(2023,11,2),
                LocalDate.of(2023,12,2),
                BigDecimal.valueOf(-1));

        String json = objectMapper.writeValueAsString(positionCommand);

        mockMvc.perform(post("/employee/80010112345")
                        .header("Authorization", format("Bearer %s", token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("invalid salary"));
    }

    @Test
    @ClearContext
    public void shouldImport100kEmployeesUnder17s() throws Exception {
        User user = new User(null, "importer", "qwerty", UserRole.IMPORTER);
        String token = jwtService.generateToken(user);

        ClassPathResource resource = new ClassPathResource("test/testingCsv/employees.csv");

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "employees.csv",
                "csv",
                resource.getInputStream());

        mockMvc.perform(multipart("/employee/import").file(file)
                        .header("Authorization", format("Bearer %s", token)))
                .andExpect(status().isAccepted());

        Thread.sleep(15000);

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

        mockMvc.perform(multipart("/employee/import").file(file)
                        .header("Authorization", format("Bearer %s", token)))
                .andExpect(status().isAccepted());

        if (databaseUtils.countRecordsInDatabase() != 15) {
            Assertions.fail("Failed rollback");
        }
    }

}

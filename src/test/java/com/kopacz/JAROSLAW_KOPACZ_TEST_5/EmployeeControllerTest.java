package com.kopacz.JAROSLAW_KOPACZ_TEST_5;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.config.ClearContext;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.config.PersonsTest;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.User;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.UserRole;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.command.PositionCommand;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.service.JwtService;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;

import static java.lang.String.format;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@PersonsTest
public class EmployeeControllerTest extends BaseIT {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final JwtService jwtService;

    @Autowired
    public EmployeeControllerTest(MockMvc mockMvc, ObjectMapper objectMapper,
                                  JwtService jwtService) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.jwtService = jwtService;
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

        mockMvc.perform(get("/person/find?type=employee&peselNumber=80010112345"))
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

        long start = System.nanoTime();

        mockMvc.perform(multipart("/employee/import").file(file)
                        .header("Authorization", format("Bearer %s", token)))
                .andExpect(status().isOk());

        long end = System.nanoTime();
        long elapsedTime = end - start;

        double elapsedTimeInSeconds = (double) elapsedTime / 1_000_000_000;

        if (elapsedTimeInSeconds > 17) {
            Assertions.fail("Waiting time is more then 17 seconds, its less then 6k inserts in second. Time: " + elapsedTimeInSeconds + " s");
        }
    }
}

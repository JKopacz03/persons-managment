package com.kopacz.JAROSLAW_KOPACZ_TEST_5;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.config.ClearContext;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.config.PersonsTest;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.User;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.UserRole;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.command.*;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.command.edit.EmployeeEditCommand;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.command.find.EmployeeFindCommand;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.service.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;

import static java.lang.String.format;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@PersonsTest
public class EmployeeCasePersonControllerTest extends BaseIT {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final JwtService jwtService;
    @Autowired
    public EmployeeCasePersonControllerTest(MockMvc mockMvc, ObjectMapper objectMapper,
                                            JwtService jwtService) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.jwtService = jwtService;
    }

    @Test
    void shouldReturnsEmployeesWorkDateFrom20220309() throws Exception {

        EmployeeFindCommand employeeCommand = new EmployeeFindCommand(
                null,
                null,
                null,
                0,
                0,
                0,
                0,
                null,
                LocalDate.of(2022,3,9),
                null,
                null,
                null,
                null,
                0,
                0
        );
        String json = objectMapper.writeValueAsString(employeeCommand);

        mockMvc.perform(get("/person/find")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
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
                .andExpect(jsonPath("$[0].numberOfProfessions").value(4))
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
                .andExpect(jsonPath("$[1].numberOfProfessions").value(5))
                .andExpect(jsonPath("$[1].numberOfPositions").value(0));

    }

    @Test
    void shouldReturnsEmployeesWorkDateTo20220209() throws Exception {

        EmployeeFindCommand employeeCommand = new EmployeeFindCommand(
                null,
                null,
                null,
                0,
                0,
                0,
                0,
                null,
                null,
                LocalDate.of(2022,2,9),
                null,
                null,
                null,
                0,
                0
        );
        String json = objectMapper.writeValueAsString(employeeCommand);

        mockMvc.perform(get("/person/find")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
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
                .andExpect(jsonPath("$[0].numberOfProfessions").value(1))
                .andExpect(jsonPath("$[0].numberOfPositions").value(0))
                .andExpect(jsonPath("$[1].firstName").value("Alice"))
                .andExpect(jsonPath("$[1].lastName").value("Smith"))
                .andExpect(jsonPath("$[1].peselNumber").value("81020223456"))
                .andExpect(jsonPath("$[1].height").value(165.0))
                .andExpect(jsonPath("$[1].weight").value(60.0))
                .andExpect(jsonPath("$[1].email").value("alicesmith@example.com"))
                .andExpect(jsonPath("$[1].workStartDate").value("2022-02-01"))
                .andExpect(jsonPath("$[1].actualProfession").value("Designer"))
                .andExpect(jsonPath("$[1].salary").value(4500.00))
                .andExpect(jsonPath("$[1].numberOfProfessions").value(2))
                .andExpect(jsonPath("$[1].numberOfPositions").value(0));
    }

    @Test
    void shouldReturnsEmployeesActualProfessionDesigner() throws Exception {

        EmployeeFindCommand employeeCommand = new EmployeeFindCommand(
                null,
                null,
                null,
                0,
                0,
                0,
                0,
                null,
                null,
                null,
                "designer",
                null,
                null,
                0,
                0
        );
        String json = objectMapper.writeValueAsString(employeeCommand);

        mockMvc.perform(get("/person/find")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
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
                .andExpect(jsonPath("$[0].numberOfProfessions").value(2))
                .andExpect(jsonPath("$[0].numberOfPositions").value(0));
    }

    @Test
    void shouldReturnsEmployeesSalaryFrom7000() throws Exception {

        EmployeeFindCommand employeeCommand = new EmployeeFindCommand(
                null,
                null,
                null,
                0,
                0,
                0,
                0,
                null,
                null,
                null,
                null,
                BigDecimal.valueOf(7000),
                null,
                0,
                0
        );
        String json = objectMapper.writeValueAsString(employeeCommand);

        mockMvc.perform(get("/person/find")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
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
                .andExpect(jsonPath("$[0].numberOfProfessions").value(5))
                .andExpect(jsonPath("$[0].numberOfPositions").value(0));
    }

    @Test
    void shouldReturnsEmployeesSalaryTo5000() throws Exception {

        EmployeeFindCommand employeeCommand = new EmployeeFindCommand(
                null,
                null,
                null,
                0,
                0,
                0,
                0,
                null,
                null,
                null,
                null,
                null,
                BigDecimal.valueOf(5000),
                0,
                0
        );
        String json = objectMapper.writeValueAsString(employeeCommand);

        mockMvc.perform(get("/person/find")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
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
                .andExpect(jsonPath("$[0].numberOfProfessions").value(1))
                .andExpect(jsonPath("$[0].numberOfPositions").value(0))
                .andExpect(jsonPath("$[1].firstName").value("Alice"))
                .andExpect(jsonPath("$[1].lastName").value("Smith"))
                .andExpect(jsonPath("$[1].peselNumber").value("81020223456"))
                .andExpect(jsonPath("$[1].height").value(165.0))
                .andExpect(jsonPath("$[1].weight").value(60.0))
                .andExpect(jsonPath("$[1].email").value("alicesmith@example.com"))
                .andExpect(jsonPath("$[1].workStartDate").value("2022-02-01"))
                .andExpect(jsonPath("$[1].actualProfession").value("Designer"))
                .andExpect(jsonPath("$[1].salary").value(4500.00))
                .andExpect(jsonPath("$[1].numberOfProfessions").value(2))
                .andExpect(jsonPath("$[1].numberOfPositions").value(0));
    }

    @Test
    void shouldReturnsEmployeesNumberOfProfessionsFrom5() throws Exception {
        EmployeeFindCommand employeeCommand = new EmployeeFindCommand(
                null,
                null,
                null,
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
                5,
                0
        );
        String json = objectMapper.writeValueAsString(employeeCommand);

        mockMvc.perform(get("/person/find")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
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
                .andExpect(jsonPath("$[0].numberOfProfessions").value(5))
                .andExpect(jsonPath("$[0].numberOfPositions").value(0));
    }

    @Test
    void shouldReturnsEmployeesNumberOfProfessionsTo1() throws Exception {
        EmployeeFindCommand employeeCommand = new EmployeeFindCommand(
                null,
                null,
                null,
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
                1
        );
        String json = objectMapper.writeValueAsString(employeeCommand);
        mockMvc.perform(get("/person/find?")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
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
                .andExpect(jsonPath("$[0].numberOfProfessions").value(1))
                .andExpect(jsonPath("$[0].numberOfPositions").value(0));
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
                BigDecimal.valueOf(3000),
                1
        );
        String json = objectMapper.writeValueAsString(employeeCommand);

        mockMvc.perform(post("/person")
                        .header("Authorization", format("Bearer %s", token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        EmployeeFindCommand employeeFindCommand = new EmployeeFindCommand(
                null,
                null,
                "81981299",
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
        String json2 = objectMapper.writeValueAsString(employeeFindCommand);

        mockMvc.perform(get("/person/find")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json2))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("Jan"))
                .andExpect(jsonPath("$[0].lastName").value("Kowalski"))
                .andExpect(jsonPath("$[0].peselNumber").value("81981299"))
                .andExpect(jsonPath("$[0].height").value(180.0))
                .andExpect(jsonPath("$[0].weight").value(90.0))
                .andExpect(jsonPath("$[0].email").value("janekkowal@gmail.com"))
                .andExpect(jsonPath("$[0].workStartDate").value("2022-12-22"))
                .andExpect(jsonPath("$[0].actualProfession").value("designer"))
                .andExpect(jsonPath("$[0].salary").value(3000.0))
                .andExpect(jsonPath("$[0].numberOfProfessions").value(1));
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
                BigDecimal.valueOf(3000),
                1
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
                BigDecimal.valueOf(3000),
                1
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
                BigDecimal.valueOf(0),
                1
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
    void missingNumberOfProfessions_shouldReturnBadRequest() throws Exception {
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
                BigDecimal.valueOf(3000),
                -1
        );
        String json = objectMapper.writeValueAsString(employeeCommand);

        mockMvc.perform(post("/person")
                        .header("Authorization", format("Bearer %s", token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("missing numberOfProfessions"));
    }

    @Test
    @ClearContext
    void shouldEditEmployeeEmailAndActualProfession() throws Exception {
        User user = new User(null, "admin", "qwerty", UserRole.ADMIN);
        String token = jwtService.generateToken(user);

        EmployeeEditCommand employeeEditCommandCommand = new EmployeeEditCommand(
                "Alice",
                "Smith",
                "81020223456",
                165.0,
                60.0,
                "alicesmith2@example.com",
                1,
                LocalDate.of(2022,2,1),
                "Call center",
                BigDecimal.valueOf(4500),
                2
        );
        String json = objectMapper.writeValueAsString(employeeEditCommandCommand);

        mockMvc.perform(patch("/person/81020223456")
                        .header("Authorization", format("Bearer %s", token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        EmployeeFindCommand employeeFindCommand = new EmployeeFindCommand(
                null,
                null,
                "81020223456",
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
        String json2 = objectMapper.writeValueAsString(employeeFindCommand);

        mockMvc.perform(get("/person/find")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json2))
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
                .andExpect(jsonPath("$[0].numberOfProfessions").value(2))
                .andExpect(jsonPath("$[0].numberOfPositions").value(0));
    }


}


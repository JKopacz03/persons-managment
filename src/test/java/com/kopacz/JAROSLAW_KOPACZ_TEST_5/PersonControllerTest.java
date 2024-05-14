package com.kopacz.JAROSLAW_KOPACZ_TEST_5;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.config.ClearContext;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.config.PersonsTest;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.User;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.UserRole;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.command.*;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.command.edit.EmployeeEditCommand;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.command.find.StudentFindCommand;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.service.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
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
@ActiveProfiles("test")
@SpringBootTest
@Testcontainers
public class PersonControllerTest {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final JwtService jwtService;
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
    public PersonControllerTest(MockMvc mockMvc, ObjectMapper objectMapper,
                                JwtService jwtService) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.jwtService = jwtService;
    }

    @Test
    void shouldReturnsStudentsWithPageSize3() throws Exception {

        StudentFindCommand studentFindCommand = new StudentFindCommand();
        String json = objectMapper.writeValueAsString(studentFindCommand);

        mockMvc.perform(get("/person/find?size=3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].lastName").value("Doe"))
                .andExpect(jsonPath("$[0].peselNumber").value("73620954781"))
                .andExpect(jsonPath("$[0].height").value(180.0))
                .andExpect(jsonPath("$[0].weight").value(75.0))
                .andExpect(jsonPath("$[0].email").value("johndoe@example.com"))
                .andExpect(jsonPath("$[0].college").value("Harvard University"))
                .andExpect(jsonPath("$[0].academicYear").value(3))
                .andExpect(jsonPath("$[0].scholarship").value(1500.00))
                .andExpect(jsonPath("$[1].firstName").value("Jane"))
                .andExpect(jsonPath("$[1].lastName").value("Smith"))
                .andExpect(jsonPath("$[1].peselNumber").value("73620954782"))
                .andExpect(jsonPath("$[1].height").value(165.0))
                .andExpect(jsonPath("$[1].weight").value(60.0))
                .andExpect(jsonPath("$[1].email").value("janesmith@example.com"))
                .andExpect(jsonPath("$[1].college").value("Stanford University"))
                .andExpect(jsonPath("$[1].academicYear").value(4))
                .andExpect(jsonPath("$[1].scholarship").value(1800.00))
                .andExpect(jsonPath("$[2].firstName").value("Michael"))
                .andExpect(jsonPath("$[2].lastName").value("Johnson"))
                .andExpect(jsonPath("$[2].peselNumber").value("73620954783"))
                .andExpect(jsonPath("$[2].height").value(190.0))
                .andExpect(jsonPath("$[2].weight").value(80.0))
                .andExpect(jsonPath("$[2].email").value("michaeljohnson@example.com"))
                .andExpect(jsonPath("$[2].college").value("MIT"))
                .andExpect(jsonPath("$[2].academicYear").value(2))
                .andExpect(jsonPath("$[2].scholarship").value(1200.00));

    }

    @Test
    void shouldReturnsStudentsWithPageSize3andNextPage() throws Exception {

        StudentFindCommand studentFindCommand = new StudentFindCommand();
        String json = objectMapper.writeValueAsString(studentFindCommand);

        mockMvc.perform(get("/person/find?size=3&page=1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("Emily"))
                .andExpect(jsonPath("$[0].lastName").value("Williams"))
                .andExpect(jsonPath("$[0].peselNumber").value("73620954784"))
                .andExpect(jsonPath("$[0].height").value(170.0))
                .andExpect(jsonPath("$[0].weight").value(65.0))
                .andExpect(jsonPath("$[0].email").value("emilywilliams@example.com"))
                .andExpect(jsonPath("$[0].college").value("Yale University"))
                .andExpect(jsonPath("$[0].academicYear").value(3))
                .andExpect(jsonPath("$[0].scholarship").value(1600.00))
                .andExpect(jsonPath("$[1].firstName").value("David"))
                .andExpect(jsonPath("$[1].lastName").value("Brown"))
                .andExpect(jsonPath("$[1].peselNumber").value("73620954785"))
                .andExpect(jsonPath("$[1].height").value(175.0))
                .andExpect(jsonPath("$[1].weight").value(70.0))
                .andExpect(jsonPath("$[1].email").value("davidbrown@example.com"))
                .andExpect(jsonPath("$[1].college").value("Columbia University"))
                .andExpect(jsonPath("$[1].academicYear").value(4))
                .andExpect(jsonPath("$[1].scholarship").value(2000.00));
    }

    @Test
    void shouldReturnsStudentsWithFirstnameEmily() throws Exception {

        StudentFindCommand studentFindCommand = new StudentFindCommand(
                "emily",
                null,
                null,
                0,
                0,
                0,
                0,
                null,
                null,
                0,
                0,
                null,
                null
        );
        String json = objectMapper.writeValueAsString(studentFindCommand);

        mockMvc.perform(get("/person/find")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("Emily"))
                .andExpect(jsonPath("$[0].lastName").value("Williams"))
                .andExpect(jsonPath("$[0].peselNumber").value("73620954784"))
                .andExpect(jsonPath("$[0].height").value(170.0))
                .andExpect(jsonPath("$[0].weight").value(65.0))
                .andExpect(jsonPath("$[0].email").value("emilywilliams@example.com"))
                .andExpect(jsonPath("$[0].college").value("Yale University"))
                .andExpect(jsonPath("$[0].academicYear").value(3))
                .andExpect(jsonPath("$[0].scholarship").value(1600.00));
    }

    @Test
    void shouldReturnsStudentsWithLastnameBrown() throws Exception {

        StudentFindCommand studentFindCommand = new StudentFindCommand(
                null,
                "brown",
                null,
                0,
                0,
                0,
                0,
                null,
                null,
                0,
                0,
                null,
                null
        );
        String json = objectMapper.writeValueAsString(studentFindCommand);

        mockMvc.perform(get("/person/find")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("David"))
                .andExpect(jsonPath("$[0].lastName").value("Brown"))
                .andExpect(jsonPath("$[0].peselNumber").value("73620954785"))
                .andExpect(jsonPath("$[0].height").value(175.0))
                .andExpect(jsonPath("$[0].weight").value(70.0))
                .andExpect(jsonPath("$[0].email").value("davidbrown@example.com"))
                .andExpect(jsonPath("$[0].college").value("Columbia University"))
                .andExpect(jsonPath("$[0].academicYear").value(4))
                .andExpect(jsonPath("$[0].scholarship").value(2000.00));;
    }

    @Test
    void shouldReturnsStudentsHeightFrom180() throws Exception {

        StudentFindCommand studentFindCommand = new StudentFindCommand(
                null,
                null,
                null,
                180,
                0,
                0,
                0,
                null,
                null,
                0,
                0,
                null,
                null
        );
        String json = objectMapper.writeValueAsString(studentFindCommand);

        mockMvc.perform(get("/person/find")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].lastName").value("Doe"))
                .andExpect(jsonPath("$[0].peselNumber").value("73620954781"))
                .andExpect(jsonPath("$[0].height").value(180.0))
                .andExpect(jsonPath("$[0].weight").value(75.0))
                .andExpect(jsonPath("$[0].email").value("johndoe@example.com"))
                .andExpect(jsonPath("$[0].college").value("Harvard University"))
                .andExpect(jsonPath("$[0].academicYear").value(3))
                .andExpect(jsonPath("$[0].scholarship").value(1500.00))
                .andExpect(jsonPath("$[1].firstName").value("Michael"))
                .andExpect(jsonPath("$[1].lastName").value("Johnson"))
                .andExpect(jsonPath("$[1].peselNumber").value("73620954783"))
                .andExpect(jsonPath("$[1].height").value(190.0))
                .andExpect(jsonPath("$[1].weight").value(80.0))
                .andExpect(jsonPath("$[1].email").value("michaeljohnson@example.com"))
                .andExpect(jsonPath("$[1].college").value("MIT"))
                .andExpect(jsonPath("$[1].academicYear").value(2))
                .andExpect(jsonPath("$[1].scholarship").value(1200.00));
    }

    @Test
    void shouldReturnsStudentsHeightTo170() throws Exception {

        StudentFindCommand studentFindCommand = new StudentFindCommand(
                null,
                null,
                null,
                0,
                170,
                0,
                0,
                null,
                null,
                0,
                0,
                null,
                null
        );
        String json = objectMapper.writeValueAsString(studentFindCommand);

        mockMvc.perform(get("/person/find")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("Jane"))
                .andExpect(jsonPath("$[0].lastName").value("Smith"))
                .andExpect(jsonPath("$[0].peselNumber").value("73620954782"))
                .andExpect(jsonPath("$[0].height").value(165.0))
                .andExpect(jsonPath("$[0].weight").value(60.0))
                .andExpect(jsonPath("$[0].email").value("janesmith@example.com"))
                .andExpect(jsonPath("$[0].college").value("Stanford University"))
                .andExpect(jsonPath("$[0].academicYear").value(4))
                .andExpect(jsonPath("$[0].scholarship").value(1800.00))
                .andExpect(jsonPath("$[1].firstName").value("Emily"))
                .andExpect(jsonPath("$[1].lastName").value("Williams"))
                .andExpect(jsonPath("$[1].peselNumber").value("73620954784"))
                .andExpect(jsonPath("$[1].height").value(170.0))
                .andExpect(jsonPath("$[1].weight").value(65.0))
                .andExpect(jsonPath("$[1].email").value("emilywilliams@example.com"))
                .andExpect(jsonPath("$[1].college").value("Yale University"))
                .andExpect(jsonPath("$[1].academicYear").value(3))
                .andExpect(jsonPath("$[1].scholarship").value(1600.00));
    }

    @Test
    void shouldReturnsStudentsHWeightFrom80() throws Exception {

        StudentFindCommand studentFindCommand = new StudentFindCommand(
                null,
                null,
                null,
                0,
                0,
                80,
                0,
                null,
                null,
                0,
                0,
                null,
                null
        );
        String json = objectMapper.writeValueAsString(studentFindCommand);

        mockMvc.perform(get("/person/find")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("Michael"))
                .andExpect(jsonPath("$[0].lastName").value("Johnson"))
                .andExpect(jsonPath("$[0].peselNumber").value("73620954783"))
                .andExpect(jsonPath("$[0].height").value(190.0))
                .andExpect(jsonPath("$[0].weight").value(80.0))
                .andExpect(jsonPath("$[0].email").value("michaeljohnson@example.com"))
                .andExpect(jsonPath("$[0].college").value("MIT"))
                .andExpect(jsonPath("$[0].academicYear").value(2))
                .andExpect(jsonPath("$[0].scholarship").value(1200.00));
    }

    @Test
    void shouldReturnsStudentsHWeightTo60() throws Exception {

        StudentFindCommand studentFindCommand = new StudentFindCommand(
                null,
                null,
                null,
                0,
                0,
                0,
                60,
                null,
                null,
                0,
                0,
                null,
                null
        );
        String json = objectMapper.writeValueAsString(studentFindCommand);

        mockMvc.perform(get("/person/find")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("Jane"))
                .andExpect(jsonPath("$[0].lastName").value("Smith"))
                .andExpect(jsonPath("$[0].peselNumber").value("73620954782"))
                .andExpect(jsonPath("$[0].height").value(165.0))
                .andExpect(jsonPath("$[0].weight").value(60.0))
                .andExpect(jsonPath("$[0].email").value("janesmith@example.com"))
                .andExpect(jsonPath("$[0].college").value("Stanford University"))
                .andExpect(jsonPath("$[0].academicYear").value(4))
                .andExpect(jsonPath("$[0].scholarship").value(1800.00));
    }

    @Test
    void shouldReturnsStudentsByPeselnumber() throws Exception {

        StudentFindCommand studentFindCommand = new StudentFindCommand(
                null,
                null,
                "73620954785",
                0,
                0,
                0,
                0,
                null,
                null,
                0,
                0,
                null,
                null
        );
        String json = objectMapper.writeValueAsString(studentFindCommand);

        mockMvc.perform(get("/person/find")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("David"))
                .andExpect(jsonPath("$[0].lastName").value("Brown"))
                .andExpect(jsonPath("$[0].peselNumber").value("73620954785"))
                .andExpect(jsonPath("$[0].height").value(175.0))
                .andExpect(jsonPath("$[0].weight").value(70.0))
                .andExpect(jsonPath("$[0].email").value("davidbrown@example.com"))
                .andExpect(jsonPath("$[0].college").value("Columbia University"))
                .andExpect(jsonPath("$[0].academicYear").value(4))
                .andExpect(jsonPath("$[0].scholarship").value(2000.00));

    }

    @Test
    void shouldReturnsStudentsByEmail() throws Exception {

        StudentFindCommand studentFindCommand = new StudentFindCommand(
                null,
                null,
                null,
                0,
                0,
                0,
                0,
                "davidbrown@example.com",
                null,
                0,
                0,
                null,
                null
        );
        String json = objectMapper.writeValueAsString(studentFindCommand);

        mockMvc.perform(get("/person/find")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(jsonPath("$[0].firstName").value("David"))
                .andExpect(jsonPath("$[0].lastName").value("Brown"))
                .andExpect(jsonPath("$[0].peselNumber").value("73620954785"))
                .andExpect(jsonPath("$[0].height").value(175.0))
                .andExpect(jsonPath("$[0].weight").value(70.0))
                .andExpect(jsonPath("$[0].email").value("davidbrown@example.com"))
                .andExpect(jsonPath("$[0].college").value("Columbia University"))
                .andExpect(jsonPath("$[0].academicYear").value(4))
                .andExpect(jsonPath("$[0].scholarship").value(2000.00));

    }

    @Test
    void shouldReturnForbidden() throws Exception {

        PensionerCommand pensionerCommand = new PensionerCommand(
                "Jan",
                "Kowalski",
                "81981297",
                180.0,
                90.0,
                "janekkowal@gmail.com",
                BigDecimal.valueOf(1000),
                60
        );
        String json = objectMapper.writeValueAsString(pensionerCommand);

        mockMvc.perform(post("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isForbidden());
    }

    @Test
    void missingFirstName_shouldReturnBadRequest() throws Exception {
        User user = new User(null, "admin", "qwerty", UserRole.ADMIN);
        String token = jwtService.generateToken(user);

        PensionerCommand pensionerCommand = new PensionerCommand(
                "",
                "Kowalski",
                "81981297",
                180.0,
                90.0,
                "janekkowal@gmail.com",
                BigDecimal.valueOf(1000),
                60
        );
        String json = objectMapper.writeValueAsString(pensionerCommand);

        mockMvc.perform(post("/person")
                        .header("Authorization", format("Bearer %s", token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("firstName is mandatory"));
    }

    @Test
    void missingLastName_shouldReturnBadRequest() throws Exception {
        User user = new User(null, "admin", "qwerty", UserRole.ADMIN);
        String token = jwtService.generateToken(user);

        PensionerCommand pensionerCommand = new PensionerCommand(
                "Jan",
                "",
                "81981297",
                180.0,
                90.0,
                "janekkowal@gmail.com",
                BigDecimal.valueOf(1000),
                60
        );
        String json = objectMapper.writeValueAsString(pensionerCommand);

        mockMvc.perform(post("/person")
                        .header("Authorization", format("Bearer %s", token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("lastName is mandatory"));
    }

    @Test
    void missingPeselNumber_shouldReturnBadRequest() throws Exception {
        User user = new User(null, "admin", "qwerty", UserRole.ADMIN);
        String token = jwtService.generateToken(user);

        PensionerCommand pensionerCommand = new PensionerCommand(
                "Jan",
                "Kowalski",
                "",
                180.0,
                90.0,
                "janekkowal@gmail.com",
                BigDecimal.valueOf(1000),
                60
        );
        String json = objectMapper.writeValueAsString(pensionerCommand);

        mockMvc.perform(post("/person")
                        .header("Authorization", format("Bearer %s", token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("peselNumber is mandatory"));
    }

    @Test
    void invalidHeight_shouldReturnBadRequest() throws Exception {
        User user = new User(null, "admin", "qwerty", UserRole.ADMIN);
        String token = jwtService.generateToken(user);

        PensionerCommand pensionerCommand = new PensionerCommand(
                "Jan",
                "Kowalski",
                "23465768753",
                -1.0,
                90.0,
                "janekkowal@gmail.com",
                BigDecimal.valueOf(1000),
                60
        );
        String json = objectMapper.writeValueAsString(pensionerCommand);

        mockMvc.perform(post("/person")
                        .header("Authorization", format("Bearer %s", token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("missing height"));
    }


    @Test
    void invalidWeight_shouldReturnBadRequest() throws Exception {
        User user = new User(null, "admin", "qwerty", UserRole.ADMIN);
        String token = jwtService.generateToken(user);

        PensionerCommand pensionerCommand = new PensionerCommand(
                "Jan",
                "Kowalski",
                "23465768753",
                90.0,
                -1.0,
                "janekkowal@gmail.com",
                BigDecimal.valueOf(1000),
                60
        );
        String json = objectMapper.writeValueAsString(pensionerCommand);

        mockMvc.perform(post("/person")
                        .header("Authorization", format("Bearer %s", token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("missing weight"));
    }

    @Test
    void missingEmail_shouldReturnBadRequest() throws Exception {
        User user = new User(null, "admin", "qwerty", UserRole.ADMIN);
        String token = jwtService.generateToken(user);

        PensionerCommand pensionerCommand = new PensionerCommand(
                "Jan",
                "Kowalski",
                "23465768753",
                90.0,
                90.0,
                "cdcsc",
                BigDecimal.valueOf(1000),
                60
        );
        String json = objectMapper.writeValueAsString(pensionerCommand);

        mockMvc.perform(post("/person")
                        .header("Authorization", format("Bearer %s", token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("missing email"));
    }

    @Test
    @WithMockUser(username = "employee", roles = "EMPLOYEE")
    void edit_shouldReturnForbidden() throws Exception {
        User user = new User(null, "admin", "qwerty", UserRole.EMPLOYEE);
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

        mockMvc.perform(patch("/person/d12bec21-0053-4438-adf2-26040f417f74")
                        .header("Authorization", format("Bearer %s", token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isForbidden());
    }
    @Test
    @ClearContext
    void edit_badVersion_shouldReturnBadRequest() throws Exception {
        User user = new User(null, "admin", "qwerty", UserRole.EMPLOYEE);
        String token = jwtService.generateToken(user);

        EmployeeEditCommand employeeEditCommandCommand = new EmployeeEditCommand(
                "Alice",
                "Smith",
                "81020223456",
                165.0,
                60.0,
                "alicesmith@example.com",
                2,
                LocalDate.of(2022,2,1),
                "Call center",
                BigDecimal.valueOf(4500),
                2
        );
        String json = objectMapper.writeValueAsString(employeeEditCommandCommand);

        mockMvc.perform(patch("/person/d12bec21-0053-4438-adf2-26040f417f74")
                        .header("Authorization", format("Bearer %s", token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

}

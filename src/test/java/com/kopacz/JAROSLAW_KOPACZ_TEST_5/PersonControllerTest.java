package com.kopacz.JAROSLAW_KOPACZ_TEST_5;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
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
public class PersonControllerTest {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final JwtService jwtService;
    @Autowired
    public PersonControllerTest(MockMvc mockMvc, ObjectMapper objectMapper,
                                JwtService jwtService) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.jwtService = jwtService;
    }
    @Test
    void badType_shouldBadRequest() throws Exception {

        mockMvc.perform(get("/person/find?type=efew"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid command type"));

    }

    @Test
    void emptyParameter_shouldBadRequest() throws Exception {

        mockMvc.perform(get("/person/find"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("type is mandatory"));

    }

    @Test
    void shouldReturnsStudentsWithPageSize3() throws Exception {

        mockMvc.perform(get("/person/find?type=student&size=3"))
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

        mockMvc.perform(get("/person/find?type=student&size=3&page=1"))
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

        mockMvc.perform(get("/person/find?type=student&firstName=emily"))
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

        mockMvc.perform(get("/person/find?type=student&lastName=brown"))
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

        mockMvc.perform(get("/person/find?type=student&heightFrom=180"))
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

        mockMvc.perform(get("/person/find?type=student&heightTo=170"))
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

        mockMvc.perform(get("/person/find?type=student&weightFrom=80"))
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

        mockMvc.perform(get("/person/find?type=student&weightTo=60"))
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

        mockMvc.perform(get("/person/find?type=student&peselNumber=73620954785"))
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

        mockMvc.perform(get("/person/find?type=student&email=davidbrown@example.com"))
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
                .andExpect(status().isForbidden());
    }
    @Test
    @ClearContext
    void edit_badVersion_shouldReturnBadRequest() throws Exception {
        User user = new User(null, "admin", "qwerty", UserRole.EMPLOYEE);
        String token = jwtService.generateToken(user);

        EmployeeEditCommand employeeEditCommandCommand = new EmployeeEditCommand(
                12L,
                "Alice",
                "Smith",
                "81020223456",
                165.0,
                60.0,
                "alicesmith@example.com",
                2,
                LocalDate.of(2022,2,1),
                "Call center",
                BigDecimal.valueOf(4500)
        );
        String json = objectMapper.writeValueAsString(employeeEditCommandCommand);

        mockMvc.perform(put("/person/12")
                        .header("Authorization", format("Bearer %s", token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    @ClearContext
    public void shouldReturnJobStatus() throws Exception {
        User user = new User(null, "importer", "qwerty", UserRole.IMPORTER);
        String token = jwtService.generateToken(user);

        ClassPathResource resource = new ClassPathResource("test/testingCsv/pensioners.csv");

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "pensioners.csv",
                "csv",
                resource.getInputStream());

        mockMvc.perform(multipart("/person/import?type=pensioner").file(file)
                        .header("Authorization", format("Bearer %s", token)))
                .andExpect(status().isAccepted());

        Thread.sleep(15000);

        mockMvc.perform(get("/person/job-status/1")
                        .header("Authorization", format("Bearer %s", token)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("COMPLETED"))
                .andExpect(jsonPath("$.processedCount").value(100000));
    }

    @Test
    public void invalidId_shouldReturnBadRequest() throws Exception {
        User user = new User(null, "importer", "qwerty", UserRole.IMPORTER);
        String token = jwtService.generateToken(user);

        mockMvc.perform(get("/person/job-status/1")
                        .header("Authorization", format("Bearer %s", token)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid id"));
    }
}

package com.kopacz.JAROSLAW_KOPACZ_TEST_5;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.config.ClearContext;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.config.PersonsTest;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.User;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.UserRole;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.command.*;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.command.edit.StudentEditCommand;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.command.find.StudentFindCommand;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.service.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;

import static java.lang.String.format;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@PersonsTest
@ActiveProfiles("test")
@SpringBootTest
@Testcontainers
public class StudentCasePersonControllerTest {
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
    public StudentCasePersonControllerTest(MockMvc mockMvc, ObjectMapper objectMapper,
                                           JwtService jwtService) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.jwtService = jwtService;
    }

    @Test
    void shouldReturnsStudentsByCollege() throws Exception {
        StudentFindCommand studentFindCommand = new StudentFindCommand(
                null,
                null,
                null,
                0,
                0,
                0,
                0,
                null,
                "columbia university",
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
    void shouldReturnsStudentsAcademicYearFrom4() throws Exception {

        StudentFindCommand studentFindCommand = new StudentFindCommand(
                null,
                null,
                null,
                0,
                0,
                0,
                0,
                null,
                null,
                4,
                0,
                null,
                null
        );
        String json = objectMapper.writeValueAsString(studentFindCommand);

        mockMvc.perform(get("/person/find")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(jsonPath("$[0].firstName").value("Jane"))
                .andExpect(jsonPath("$[0].lastName").value("Smith"))
                .andExpect(jsonPath("$[0].peselNumber").value("73620954782"))
                .andExpect(jsonPath("$[0].height").value(165.0))
                .andExpect(jsonPath("$[0].weight").value(60.0))
                .andExpect(jsonPath("$[0].email").value("janesmith@example.com"))
                .andExpect(jsonPath("$[0].college").value("Stanford University"))
                .andExpect(jsonPath("$[0].academicYear").value(4))
                .andExpect(jsonPath("$[0].scholarship").value(1800.00))
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
    void shouldReturnsStudentsAcademicYearTo2() throws Exception {

        StudentFindCommand studentFindCommand = new StudentFindCommand(
                null,
                null,
                null,
                0,
                0,
                0,
                0,
                null,
                null,
                0,
                2,
                null,
                null
        );
        String json = objectMapper.writeValueAsString(studentFindCommand);

        mockMvc.perform(get("/person/find")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
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
    void shouldReturnsStudentsScholarshipFrom2000() throws Exception {

        StudentFindCommand studentFindCommand = new StudentFindCommand(
                null,
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
                BigDecimal.valueOf(2000),
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
    void shouldReturnsStudentsScholarshipTo1200() throws Exception {

        StudentFindCommand studentFindCommand = new StudentFindCommand(
                null,
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
                BigDecimal.valueOf(1200)
        );
        String json = objectMapper.writeValueAsString(studentFindCommand);

        mockMvc.perform(get("/person/find")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
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
    @ClearContext
    void shouldAddStudent() throws Exception {
        User user = new User(null, "admin", "qwerty", UserRole.ADMIN);
        String token = jwtService.generateToken(user);

        StudentCommand studentCommand = new StudentCommand(
                "Jan",
                "Kowalski",
                "81981298",
                180.0,
                90.0,
                "janekkowal@gmail.com",
                "example college",
                2,
                BigDecimal.valueOf(3000)
        );
        String json = objectMapper.writeValueAsString(studentCommand);

        mockMvc.perform(post("/person")
                        .header("Authorization", format("Bearer %s", token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        StudentFindCommand studentFindCommand = new StudentFindCommand(
                null,
                null,
                "81981298",
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
        String json2 = objectMapper.writeValueAsString(studentFindCommand);

        mockMvc.perform(get("/person/find")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json2))
                .andExpect(jsonPath("$[0].firstName").value("Jan"))
                .andExpect(jsonPath("$[0].lastName").value("Kowalski"))
                .andExpect(jsonPath("$[0].peselNumber").value("81981298"))
                .andExpect(jsonPath("$[0].height").value(180.0))
                .andExpect(jsonPath("$[0].weight").value(90.0))
                .andExpect(jsonPath("$[0].email").value("janekkowal@gmail.com"))
                .andExpect(jsonPath("$[0].college").value("example college"))
                .andExpect(jsonPath("$[0].academicYear").value(2))
                .andExpect(jsonPath("$[0].scholarship").value(3000.0));
    }

    @Test
    void missingCollege_shouldReturnBadRequest() throws Exception {
        User user = new User(null, "admin", "qwerty", UserRole.ADMIN);
        String token = jwtService.generateToken(user);

        StudentCommand studentCommand = new StudentCommand(
                "Jan",
                "Kowalski",
                "81981298",
                180.0,
                90.0,
                "janekkowal@gmail.com",
                "",
                2,
                BigDecimal.valueOf(3000)
        );
        String json = objectMapper.writeValueAsString(studentCommand);

        mockMvc.perform(post("/person")
                        .header("Authorization", format("Bearer %s", token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("college is mandatory"));
    }

    @Test
    void missingAcademicYear_shouldReturnBadRequest() throws Exception {
        User user = new User(null, "admin", "qwerty", UserRole.ADMIN);
        String token = jwtService.generateToken(user);

        StudentCommand studentCommand = new StudentCommand(
                "Jan",
                "Kowalski",
                "81981298",
                180.0,
                90.0,
                "janekkowal@gmail.com",
                "example college",
                -1,
                BigDecimal.valueOf(3000)
        );
        String json = objectMapper.writeValueAsString(studentCommand);

        mockMvc.perform(post("/person")
                        .header("Authorization", format("Bearer %s", token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("missing academicYear"));
    }

    @Test
    void missingScholarship_shouldReturnBadRequest() throws Exception {
        User user = new User(null, "admin", "qwerty", UserRole.ADMIN);
        String token = jwtService.generateToken(user);

        StudentCommand studentCommand = new StudentCommand(
                "Jan",
                "Kowalski",
                "81981298",
                180.0,
                90.0,
                "janekkowal@gmail.com",
                "example college",
                1,
                BigDecimal.valueOf(0)
        );
        String json = objectMapper.writeValueAsString(studentCommand);

        mockMvc.perform(post("/person")
                        .header("Authorization", format("Bearer %s", token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("missing scholarship"));
    }

    @Test
    @ClearContext
    void shouldEditStudentFirstNameAndCollege() throws Exception {
        User user = new User(null, "admin", "qwerty", UserRole.ADMIN);
        String token = jwtService.generateToken(user);

        StudentFindCommand studentFindCommand = new StudentFindCommand(
                null,
                null,
                "73620954782",
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
        String json2 = objectMapper.writeValueAsString(studentFindCommand);

        mockMvc.perform(get("/person/find")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json2))
                .andExpect(jsonPath("$[0].firstName").value("Jane"))
                .andExpect(jsonPath("$[0].lastName").value("Smith"))
                .andExpect(jsonPath("$[0].peselNumber").value("73620954782"))
                .andExpect(jsonPath("$[0].height").value(165.0))
                .andExpect(jsonPath("$[0].weight").value(60.0))
                .andExpect(jsonPath("$[0].email").value("janesmith@example.com"))
                .andExpect(jsonPath("$[0].college").value("Stanford University"))
                .andExpect(jsonPath("$[0].academicYear").value(4))
                .andExpect(jsonPath("$[0].scholarship").value(1800.00));


        StudentEditCommand studentCommand = new StudentEditCommand(
                "Josh",
                "Smith",
                "73620954782",
                165.0,
                60.0,
                "janesmith@example.com",
                1,
                "Miami University",
                4,
                BigDecimal.valueOf(1800)
        );
        String json = objectMapper.writeValueAsString(studentCommand);

        mockMvc.perform(patch("/person/ef91ab8c-fa63-42c3-8ab6-4382106893bf")
                        .header("Authorization", format("Bearer %s", token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        mockMvc.perform(get("/person/find")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json2))
                .andExpect(jsonPath("$[0].firstName").value("Josh"))
                .andExpect(jsonPath("$[0].lastName").value("Smith"))
                .andExpect(jsonPath("$[0].peselNumber").value("73620954782"))
                .andExpect(jsonPath("$[0].height").value(165.0))
                .andExpect(jsonPath("$[0].weight").value(60.0))
                .andExpect(jsonPath("$[0].email").value("janesmith@example.com"))
                .andExpect(jsonPath("$[0].college").value("Miami University"))
                .andExpect(jsonPath("$[0].academicYear").value(4))
                .andExpect(jsonPath("$[0].scholarship").value(1800.00));

    }

}

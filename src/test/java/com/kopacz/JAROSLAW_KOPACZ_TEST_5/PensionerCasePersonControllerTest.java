package com.kopacz.JAROSLAW_KOPACZ_TEST_5;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.config.ClearContext;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.config.DatabaseUtils;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.config.PersonsTest;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.User;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.UserRole;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.command.*;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.command.edit.PensionerEditCommand;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.service.JwtService;
import org.junit.jupiter.api.*;
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

import static java.lang.String.format;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@PersonsTest
@ActiveProfiles("testH2")
@SpringBootTest
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PensionerCasePersonControllerTest {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final JwtService jwtService;
    private final DatabaseUtils databaseUtils;

    @Autowired
    public PensionerCasePersonControllerTest(MockMvc mockMvc, ObjectMapper objectMapper, JwtService jwtService, DatabaseUtils databaseUtils) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.jwtService = jwtService;
        this.databaseUtils = databaseUtils;
    }

    @Test
    void shouldReturnsPensionersWorkYearsFrom40() throws Exception {

        mockMvc.perform(get("/person/find?type=pensioner&workYearsFrom=40"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("Jane"))
                .andExpect(jsonPath("$[0].lastName").value("Smith"))
                .andExpect(jsonPath("$[0].peselNumber").value("88020223456"))
                .andExpect(jsonPath("$[0].height").value(165.0))
                .andExpect(jsonPath("$[0].weight").value(60.0))
                .andExpect(jsonPath("$[0].email").value("janesmith@example.com"))
                .andExpect(jsonPath("$[0].pensionValue").value(2800.00))
                .andExpect(jsonPath("$[0].workYears").value(40))
                .andExpect(jsonPath("$[1].firstName").value("Michael"))
                .andExpect(jsonPath("$[1].lastName").value("Johnson"))
                .andExpect(jsonPath("$[1].peselNumber").value("95030334567"))
                .andExpect(jsonPath("$[1].height").value(190.0))
                .andExpect(jsonPath("$[1].weight").value(80.0))
                .andExpect(jsonPath("$[1].email").value("michaeljohnson@example.com"))
                .andExpect(jsonPath("$[1].pensionValue").value(3000.00))
                .andExpect(jsonPath("$[1].workYears").value(45))
                .andExpect(jsonPath("$[2].firstName").value("David"))
                .andExpect(jsonPath("$[2].lastName").value("Brown"))
                .andExpect(jsonPath("$[2].peselNumber").value("90050556789"))
                .andExpect(jsonPath("$[2].height").value(175.0))
                .andExpect(jsonPath("$[2].weight").value(70.0))
                .andExpect(jsonPath("$[2].email").value("davidbrown@example.com"))
                .andExpect(jsonPath("$[2].pensionValue").value(3200.00))
                .andExpect(jsonPath("$[2].workYears").value(42));
    }

    @Test
    void shouldReturnsPensionersWorkYearsTo35() throws Exception {

        mockMvc.perform(get("/person/find?type=pensioner&workYearsTo=35"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].lastName").value("Doe"))
                .andExpect(jsonPath("$[0].peselNumber").value("91010112345"))
                .andExpect(jsonPath("$[0].height").value(180.0))
                .andExpect(jsonPath("$[0].weight").value(75.0))
                .andExpect(jsonPath("$[0].email").value("johndoe@example.com"))
                .andExpect(jsonPath("$[0].pensionValue").value(2500.00))
                .andExpect(jsonPath("$[0].workYears").value(35));
    }

    @Test
    void shouldReturnsPensionersPensionValueFrom3000() throws Exception {

        mockMvc.perform(get("/person/find?type=pensioner&pensionValueFrom=3000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("Michael"))
                .andExpect(jsonPath("$[0].lastName").value("Johnson"))
                .andExpect(jsonPath("$[0].peselNumber").value("95030334567"))
                .andExpect(jsonPath("$[0].height").value(190.0))
                .andExpect(jsonPath("$[0].weight").value(80.0))
                .andExpect(jsonPath("$[0].email").value("michaeljohnson@example.com"))
                .andExpect(jsonPath("$[0].pensionValue").value(3000.00))
                .andExpect(jsonPath("$[0].workYears").value(45))
                .andExpect(jsonPath("$[1].firstName").value("David"))
                .andExpect(jsonPath("$[1].lastName").value("Brown"))
                .andExpect(jsonPath("$[1].peselNumber").value("90050556789"))
                .andExpect(jsonPath("$[1].height").value(175.0))
                .andExpect(jsonPath("$[1].weight").value(70.0))
                .andExpect(jsonPath("$[1].email").value("davidbrown@example.com"))
                .andExpect(jsonPath("$[1].pensionValue").value(3200.00))
                .andExpect(jsonPath("$[1].workYears").value(42));
    }

    @Test
    void shouldReturnsPensionersPensionValueTo3000() throws Exception {

        mockMvc.perform(get("/person/find?type=pensioner&pensionValueTo=3000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].lastName").value("Doe"))
                .andExpect(jsonPath("$[0].peselNumber").value("91010112345"))
                .andExpect(jsonPath("$[0].height").value(180.0))
                .andExpect(jsonPath("$[0].weight").value(75.0))
                .andExpect(jsonPath("$[0].email").value("johndoe@example.com"))
                .andExpect(jsonPath("$[0].pensionValue").value(2500.00))
                .andExpect(jsonPath("$[0].workYears").value(35))
                .andExpect(jsonPath("$[1].firstName").value("Jane"))
                .andExpect(jsonPath("$[1].lastName").value("Smith"))
                .andExpect(jsonPath("$[1].peselNumber").value("88020223456"))
                .andExpect(jsonPath("$[1].height").value(165.0))
                .andExpect(jsonPath("$[1].weight").value(60.0))
                .andExpect(jsonPath("$[1].email").value("janesmith@example.com"))
                .andExpect(jsonPath("$[1].pensionValue").value(2800.00))
                .andExpect(jsonPath("$[1].workYears").value(40))
                .andExpect(jsonPath("$[2].firstName").value("Michael"))
                .andExpect(jsonPath("$[2].lastName").value("Johnson"))
                .andExpect(jsonPath("$[2].peselNumber").value("95030334567"))
                .andExpect(jsonPath("$[2].height").value(190.0))
                .andExpect(jsonPath("$[2].weight").value(80.0))
                .andExpect(jsonPath("$[2].email").value("michaeljohnson@example.com"))
                .andExpect(jsonPath("$[2].pensionValue").value(3000.00))
                .andExpect(jsonPath("$[2].workYears").value(45))
                .andExpect(jsonPath("$[3].firstName").value("Emily"))
                .andExpect(jsonPath("$[3].lastName").value("Williams"))
                .andExpect(jsonPath("$[3].peselNumber").value("92040445678"))
                .andExpect(jsonPath("$[3].height").value(170.0))
                .andExpect(jsonPath("$[3].weight").value(65.0))
                .andExpect(jsonPath("$[3].email").value("emilywilliams@example.com"))
                .andExpect(jsonPath("$[3].pensionValue").value(2700.00))
                .andExpect(jsonPath("$[3].workYears").value(38));
    }

    @Test
    @ClearContext
    void shouldAddPensioner() throws Exception {
        User user = new User(null, "admin", "qwerty", UserRole.ADMIN);
        String token = jwtService.generateToken(user);

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
                        .header("Authorization", format("Bearer %s", token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("Jan"))
                .andExpect(jsonPath("$.lastName").value("Kowalski"))
                .andExpect(jsonPath("$.peselNumber").value("81981297"))
                .andExpect(jsonPath("$.height").value(180.0))
                .andExpect(jsonPath("$.weight").value(90.0))
                .andExpect(jsonPath("$.email").value("janekkowal@gmail.com"))
                .andExpect(jsonPath("$.pensionValue").value(1000.0))
                .andExpect(jsonPath("$.workYears").value(60));
    }

    @Test
    void missingPensionValue_shouldReturnBadRequest() throws Exception {
        User user = new User(null, "admin", "qwerty", UserRole.ADMIN);
        String token = jwtService.generateToken(user);

        PensionerCommand pensionerCommand = new PensionerCommand(
                "Jan",
                "Kowalski",
                "23465768753",
                90.0,
                90.0,
                "jankowal@gmail.com",
                BigDecimal.valueOf(-1),
                60
        );
        String json = objectMapper.writeValueAsString(pensionerCommand);

        mockMvc.perform(post("/person")
                        .header("Authorization", format("Bearer %s", token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("missing pensionValue"));
    }

    @Test
    void missingWorkYears_shouldReturnBadRequest() throws Exception {
        User user = new User(null, "admin", "qwerty", UserRole.ADMIN);
        String token = jwtService.generateToken(user);

        PensionerCommand pensionerCommand = new PensionerCommand(
                "Jan",
                "Kowalski",
                "23465768753",
                90.0,
                90.0,
                "jankowal@gmail.com",
                BigDecimal.valueOf(1000),
                -1
        );
        String json = objectMapper.writeValueAsString(pensionerCommand);

        mockMvc.perform(post("/person")
                        .header("Authorization", format("Bearer %s", token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("missing workYears"));
    }

    @Test
    @ClearContext
    void shouldEditPensionersWorkYearsAndPensionValue() throws Exception {
        User user = new User(null, "admin", "qwerty", UserRole.ADMIN);
        String token = jwtService.generateToken(user);

        PensionerEditCommand pensionerCommand = new PensionerEditCommand(
                6L,
                "John",
                "Doe",
                "91010112345",
                180.0,
                75.0,
                "johndoe@example.com",
                1,
                BigDecimal.valueOf(2000),
                70
        );
        String json = objectMapper.writeValueAsString(pensionerCommand);

        mockMvc.perform(put("/person/6")
                        .header("Authorization", format("Bearer %s", token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        mockMvc.perform(get("/person/find?type=pensioner&peselNumber=91010112345"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].lastName").value("Doe"))
                .andExpect(jsonPath("$[0].peselNumber").value("91010112345"))
                .andExpect(jsonPath("$[0].height").value(180.0))
                .andExpect(jsonPath("$[0].weight").value(75.0))
                .andExpect(jsonPath("$[0].email").value("johndoe@example.com"))
                .andExpect(jsonPath("$[0].pensionValue").value(2000.00))
                .andExpect(jsonPath("$[0].workYears").value(70));
    }

    @Test
    @ClearContext
    @Order(1)
    public void shouldImport100kPensionersUnder3s() throws Exception {
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

        ClassPathResource resource = new ClassPathResource("test/testingCsv/invalidPensioners.csv");

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "invalidPensioners.csv",
                "csv",
                resource.getInputStream());

        mockMvc.perform(multipart("/person/import?type=pensioner").file(file)
                        .header("Authorization", format("Bearer %s", token)))
                .andExpect(status().isAccepted());

        if (databaseUtils.countRecordsInDatabase() != 15) {
            Assertions.fail("Failed rollback " + databaseUtils.countRecordsInDatabase());
        }
    }
}

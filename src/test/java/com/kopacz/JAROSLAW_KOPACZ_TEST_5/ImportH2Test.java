package com.kopacz.JAROSLAW_KOPACZ_TEST_5;

import com.kopacz.JAROSLAW_KOPACZ_TEST_5.config.ClearContext;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.config.DatabaseUtils;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.config.PersonsTest;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.User;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.UserRole;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.service.JwtService;
import liquibase.exception.LiquibaseException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static java.lang.String.format;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ActiveProfiles("testH2")
@SpringBootTest
@PersonsTest
public class ImportH2Test {
    private final MockMvc mockMvc;
    private final JwtService jwtService;
    private final DatabaseUtils databaseUtils;

    @Autowired
    public ImportH2Test(MockMvc mockMvc, JwtService jwtService, DatabaseUtils databaseUtils) {
        this.mockMvc = mockMvc;
        this.jwtService = jwtService;
        this.databaseUtils = databaseUtils;
    }

    @Test
    @ClearContext
    public void shouldImport100kStudentsUnder3s() throws Exception {
        User user = new User(null, "importer", "qwerty", UserRole.IMPORTER);
        String token = jwtService.generateToken(user);

        ClassPathResource resource = new ClassPathResource("test/testingCsv/students.csv");

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "students.csv",
                "csv",
                resource.getInputStream());

        mockMvc.perform(multipart("/person/import?type=student").file(file)
                        .header("Authorization", format("Bearer %s", token)))
                .andExpect(status().isAccepted());

        Thread.sleep(3500);

        if (databaseUtils.countRecordsInDatabase() != 100015) {
            Assertions.fail("Missing imports");
        }
    }
}

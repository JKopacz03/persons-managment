package com.kopacz.JAROSLAW_KOPACZ_TEST_5;

import com.kopacz.JAROSLAW_KOPACZ_TEST_5.config.ClearContext;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.config.DatabaseUtils;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.config.PersonsTest;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.User;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.UserRole;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.service.JwtService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import static java.lang.String.format;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@PersonsTest
public class StudentControllerTest extends BaseIT {
    private final MockMvc mockMvc;
    private final JwtService jwtService;
    private final DatabaseUtils databaseUtils;

    @Autowired
    public StudentControllerTest(MockMvc mockMvc, JwtService jwtService, DatabaseUtils databaseUtils) {
        this.mockMvc = mockMvc;
        this.jwtService = jwtService;
        this.databaseUtils = databaseUtils;
    }

    @Test
    @ClearContext
    public void shouldImport100kStudentsUnder15s() throws Exception {
        User user = new User(null, "importer", "qwerty", UserRole.IMPORTER);
        String token = jwtService.generateToken(user);

        ClassPathResource resource = new ClassPathResource("test/testingCsv/students.csv");

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "students.csv",
                "csv",
                resource.getInputStream());

        mockMvc.perform(multipart("/student/import").file(file)
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

        ClassPathResource resource = new ClassPathResource("test/testingCsv/invalidStudents.csv");

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "invalidStudents.csv",
                "csv",
                resource.getInputStream());

        mockMvc.perform(multipart("/student/import").file(file)
                        .header("Authorization", format("Bearer %s", token)))
                .andExpect(status().isAccepted());

        if (databaseUtils.countRecordsInDatabase() != 15) {
            Assertions.fail("Failed rollback " + databaseUtils.countRecordsInDatabase());
        }
    }
}

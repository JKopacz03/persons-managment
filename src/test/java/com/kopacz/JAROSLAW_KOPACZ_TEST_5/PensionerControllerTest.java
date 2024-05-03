package com.kopacz.JAROSLAW_KOPACZ_TEST_5;

import com.kopacz.JAROSLAW_KOPACZ_TEST_5.config.ClearContext;
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
public class PensionerControllerTest extends BaseIT {
    private final MockMvc mockMvc;
    private final JwtService jwtService;

    @Autowired
    public PensionerControllerTest(MockMvc mockMvc, JwtService jwtService) {
        this.mockMvc = mockMvc;
        this.jwtService = jwtService;
    }

    @Test
    @ClearContext
    public void shouldImport100kPensionersUnder17s() throws Exception {
        User user = new User(null, "importer", "qwerty", UserRole.IMPORTER);
        String token = jwtService.generateToken(user);

        ClassPathResource resource = new ClassPathResource("test/testingCsv/pensioners.csv");

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "pensioners.csv",
                "csv",
                resource.getInputStream());

        long start = System.nanoTime();

        mockMvc.perform(multipart("/pensioner/import").file(file)
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


package com.kopacz.JAROSLAW_KOPACZ_TEST_5;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.config.ClearContext;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.config.PersonsTest;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.command.AuthCommand;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.command.RegisterCommand;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@PersonsTest
@ActiveProfiles("testH2")
@SpringBootTest
@Testcontainers
public class AuthControllerTest {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @Autowired
    public AuthControllerTest(MockMvc mockMvc, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }
    @Test
    @ClearContext
    void shouldRegister() throws Exception {
        RegisterCommand registerCommand = new RegisterCommand(
                "aleksander", "qwerty", "ADMIN"
        );

        String json = objectMapper.writeValueAsString(registerCommand);

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());
    }
    
    @Test
    void invalidUsername_shouldReturnsBadRequest() throws Exception {
        RegisterCommand registerCommand = new RegisterCommand(
                "", "qwerty", "ADMIN"
        );

        String json = objectMapper.writeValueAsString(registerCommand);

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("username is mandatory"));
    }

    @Test
    void invalidPassword_shouldReturnsBadRequest() throws Exception {
        RegisterCommand registerCommand = new RegisterCommand(
                "aleksander", "", "ADMIN"
        );

        String json = objectMapper.writeValueAsString(registerCommand);

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("password is mandatory"));
    }

    @Test
    void invalidRole_shouldReturnsBadRequest() throws Exception {
        RegisterCommand registerCommand = new RegisterCommand(
                "aleksander", "qwerty", "dwdw"
        );

        String json = objectMapper.writeValueAsString(registerCommand);

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("bad role"));
    }

    @Test
    void auth_invalidUsername_shouldReturnsBadRequest() throws Exception {
        AuthCommand authCommand = new AuthCommand(
                "", "qwerty"
        );

        String json = objectMapper.writeValueAsString(authCommand);

        mockMvc.perform(post("/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("username is mandatory"));
    }

    @Test
    void auth_invalidPassword_shouldReturnsBadRequest() throws Exception {
        AuthCommand authCommand = new AuthCommand(
                "aleksander", ""
        );

        String json = objectMapper.writeValueAsString(authCommand);

        mockMvc.perform(post("/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("password is mandatory"));
    }

    @Test
    void shouldAuth() throws Exception {
        AuthCommand authCommand = new AuthCommand(
                "admin", "qwerty"
        );

        String json = objectMapper.writeValueAsString(authCommand);

        mockMvc.perform(post("/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }
}


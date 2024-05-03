package com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.command;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterCommand {
    @NotBlank(message = "username is mandatory")
    private String username;
    @NotBlank(message = "password is mandatory")
    private String password;
    @Pattern(regexp = "ADMIN|IMPORTER|EMPLOYEE", message = "bad role")
    private String role;
}

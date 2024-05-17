package com.kopacz.JAROSLAW_KOPACZ_TEST_5.controller;

import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.User;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.UserRole;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.command.AuthCommand;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.command.RegisterCommand;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.dto.AuthDto;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final ModelMapper modelMapper;

    @PostMapping("/register")
    public ResponseEntity<AuthDto> register(@Valid @RequestBody RegisterCommand command){
        User user = modelMapper.map(command, User.class);
        user.setRole(UserRole.valueOf(command.getRole()));
        authService.save(user);
        return new ResponseEntity<>(authService.save(user), HttpStatus.CREATED);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthDto> authenticate(@Valid @RequestBody AuthCommand command){
        return ResponseEntity.ok(authService.authenticate(command));
    }
}

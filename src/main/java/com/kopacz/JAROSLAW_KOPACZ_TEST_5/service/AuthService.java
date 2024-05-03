package com.kopacz.JAROSLAW_KOPACZ_TEST_5.service;

import com.kopacz.JAROSLAW_KOPACZ_TEST_5.exceptions.NotExistingUserException;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.User;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.command.AuthCommand;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.dto.AuthDto;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public void save(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public AuthDto authenticate(AuthCommand command) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        command.getUsername(),
                        command.getPassword()
                )
        );
        final User user = userRepository.findByUsername(command.getUsername())
                .orElseThrow(NotExistingUserException::new);
        final String jwtToken = jwtService.generateToken(user);
        return new AuthDto(jwtToken);
    }
}

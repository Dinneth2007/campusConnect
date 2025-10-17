package com.example.CampusConnectService.service;




import com.example.CampusConnectService.dto.LoginRequestDto;
import com.example.CampusConnectService.dto.UserRegistrationDto;
import com.example.CampusConnectService.entity.User;
import com.example.CampusConnectService.repository.UserRepository;
import com.example.CampusConnectService.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    public User register(UserRegistrationDto dto) {
        return userService.register(dto.getEmail(), dto.getPassword());
    }

    public String login(LoginRequestDto dto) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
            );
            // generate JWT
            return jwtTokenProvider.createToken(auth);
        } catch (org.springframework.security.core.AuthenticationException ex) {
            // normalize the exception type to BadCredentials for the controller to map to 401
            throw new BadCredentialsException("Invalid email or password");
        }
    }
}

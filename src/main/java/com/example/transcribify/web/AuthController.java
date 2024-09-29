package com.example.transcribify.web;

import com.example.transcribify.config.UserAuthenticationProvider;
import com.example.transcribify.model.dto.CredentialsDto;
import com.example.transcribify.model.dto.SignUpDto;
import com.example.transcribify.model.dto.UserDto;
import com.example.transcribify.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174"})
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;
    private final UserAuthenticationProvider userAuthenticationProvider;

    public AuthController(AuthService authService, UserAuthenticationProvider userAuthenticationProvider) {
        this.authService = authService;
        this.userAuthenticationProvider = userAuthenticationProvider;
    }

    @PostMapping("/signup")
    public ResponseEntity<UserDto> signup(@Valid @ModelAttribute SignUpDto signUpDto) {
        UserDto createdUser;
        try {
            createdUser = authService.signup(signUpDto);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
        String token = userAuthenticationProvider.createToken(signUpDto.getEmail());

        // Set token as HTTP-only, Secure cookie
        ResponseCookie cookie = ResponseCookie.from("jwt", token)
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/")
                .maxAge(3600)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(createdUser);
    }

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@Valid @RequestBody CredentialsDto credentialsDto){
        UserDto userDto = authService.login(credentialsDto);
        String token = userAuthenticationProvider.createToken(userDto.getEmail());

        // Set token as HTTP-only, Secure cookie
        ResponseCookie cookie = ResponseCookie.from("jwt", token)
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/")
                .maxAge(3600)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(userDto);
    }

    @DeleteMapping("/logout")
    public ResponseEntity<Void> logout(){
        ResponseCookie cookie = ResponseCookie.from("jwt", null)
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/")
                .maxAge(0)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }
}

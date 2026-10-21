package com.juandidev.biblioteca_api.controller;

import com.juandidev.biblioteca_api.dto.auth.AuthResponseDTO;
import com.juandidev.biblioteca_api.dto.auth.LoginRequestDTO;
import com.juandidev.biblioteca_api.dto.auth.RegistroRequestDTO;
import com.juandidev.biblioteca_api.service.IAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final IAuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> registrar(@Valid @RequestBody RegistroRequestDTO dto){
        AuthResponseDTO response = authService.registrar(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


@PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO dto){
        AuthResponseDTO response = authService.login(dto);
        return ResponseEntity.ok(response);
}

}

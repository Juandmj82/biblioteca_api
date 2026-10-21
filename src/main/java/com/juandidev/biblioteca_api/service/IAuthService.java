package com.juandidev.biblioteca_api.service;

import com.juandidev.biblioteca_api.dto.auth.AuthResponseDTO;
import com.juandidev.biblioteca_api.dto.auth.LoginRequestDTO;
import com.juandidev.biblioteca_api.dto.auth.RegistroRequestDTO;

public interface IAuthService {
    AuthResponseDTO registrar(RegistroRequestDTO dto);
    AuthResponseDTO login(LoginRequestDTO dto);
}

package com.juandidev.biblioteca_api.service;

import com.juandidev.biblioteca_api.dto.auth.AuthResponseDTO;
import com.juandidev.biblioteca_api.dto.auth.LoginRequestDTO;
import com.juandidev.biblioteca_api.dto.auth.RegistroRequestDTO;
import com.juandidev.biblioteca_api.model.Usuario;
import com.juandidev.biblioteca_api.repository.UsuarioRepository;
import com.juandidev.biblioteca_api.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements IAuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthResponseDTO registrar(RegistroRequestDTO dto) {
        // 1. Validar si el email ya existe en la base de datos
        if (usuarioRepository.findByEmail(dto.email()).isPresent()) {
            throw new IllegalArgumentException("El email ya se encuentra registrado: " + dto.email());
        }

        // 2. Construir la entidad encriptando la contraseña con BCrypt
        Usuario usuario = Usuario.builder()
                .email(dto.email())
                .password(passwordEncoder.encode(dto.password())) // ¡Encriptación segura!
                .rol(dto.rol())
                .build();

        // 3. Guardar en PostgreSQL
        usuarioRepository.save(usuario);

        // 4. Fabricar el token JWT
        String token = jwtService.generateToken(usuario);

        // 5. Devolver el DTO con el token
        return new AuthResponseDTO(token);
    }

    @Override
    public AuthResponseDTO login(LoginRequestDTO dto) {
        // 1. Autenticar credenciales usando AuthenticationManager de Spring Security
        // Si el password está mal o el email no existe, lanza BadCredentialsException
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.email(), dto.password())
        );

        // 2. Si la autenticación pasó, buscamos al usuario en la base de datos
        Usuario usuario = usuarioRepository.findByEmail(dto.email())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con email: " + dto.email()));

        // 3. Fabricar el token JWT
        String token = jwtService.generateToken(usuario);

        // 4. Devolver el DTO con el token
        return new AuthResponseDTO(token);
    }
}
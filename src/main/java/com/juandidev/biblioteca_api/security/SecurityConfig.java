package com.juandidev.biblioteca_api.security;

import com.juandidev.biblioteca_api.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final UsuarioRepository usuarioRepository;

    // 1. Cómo buscar al usuario en la BD por email
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> usuarioRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));
    }

    // 2. Encriptador de contraseñas (BCrypt: estándar de la industria)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 3. Proveedor que conecta el UserDetailsService con el PasswordEncoder
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    // 4. El AuthenticationManager que usará el servicio de Login
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // 5. La Cadena de Reglas de Seguridad (SecurityFilterChain)
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Deshabilitamos CSRF porque las APIs REST con JWT son Stateless (no usan cookies de sesión)
                .csrf(csrf -> csrf.disable())

                // Reglas de autorización en los endpoints
                .authorizeHttpRequests(auth -> auth
                        // Rutas públicas: registro y login
                        .requestMatchers("/auth/**").permitAll()
                        // Cualquier otra ruta requiere estar autenticado
                        .anyRequest().authenticated()
                )

                // No crear sesiones en memoria (100% Stateless)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // Conectar el AuthenticationProvider
                .authenticationProvider(authenticationProvider())

                // Insertar nuestro filtro JWT ANTES del filtro estándar de usuario/password
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
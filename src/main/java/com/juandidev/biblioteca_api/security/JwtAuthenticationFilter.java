package com.juandidev.biblioteca_api.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        // 1. Obtener el encabezado Authorization de la petición HTTP
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;
        // Si no hay header o no empieza con "Bearer ", pasamos la petición al siguiente filtro
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        // 2. Extraer el token (cortando los primeros 7 caracteres: "Bearer ")
        jwt = authHeader.substring(7);

        // 3. Manejo defensivo: Intentar extraer el email y validar el token.
        // Si el cliente envía un token malformado, expirado o con firma alterada,
        // la librería JJWT lanzará una JwtException. Atrapamos el error para que NO
        // reviente la consola del servidor con un error 500, sino que simplemente no autenticamos
        // y dejamos que la cadena continúe (Spring Security responderá 403 Forbidden).
        try {
            userEmail = jwtService.extractUsername(jwt);

            // 4. Si hay email y el usuario AÚN no está autenticado en el contexto de Spring:
            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // Cargamos los datos del usuario desde la base de datos
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

                // 5. Si el token es válido (firma correcta y no expirado):
                if (jwtService.isTokenValid(jwt, userDetails)) {
                    // Creamos la credencial de autenticación con el usuario y sus roles/permisos
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    // Le adjuntamos los detalles de la petición HTTP (IP, etc.)
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // ¡AUTORIZADO!: Registramos la autenticación en el contexto de Spring Security
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (Exception e) {
            // Token inválido o malformado: no establecemos autenticación en el SecurityContext
            // El usuario continuará como anónimo y Spring Security lo bloqueará limpiamente con 403/401.
        }

        // Continuamos la cadena de filtros hacia el Controller
        filterChain.doFilter(request, response);
    }
}
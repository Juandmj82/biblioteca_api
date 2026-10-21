package com.juandidev.biblioteca_api.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

/**
 * Servicio encargado de la criptografía y gestión de tokens JWT (JSON Web Tokens).
 * Utiliza la librería JJWT (0.12.x) con algoritmo HMAC-SHA256.
 */
@Service
public class JwtService {

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    /**
     * 1. GENERAR TOKEN:
     * Crea un JWT firmado con los datos del usuario.
     * Guarda el username/email como Subject, la fecha actual de emisión y la fecha de expiración.
     */
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .subject(userDetails.getUsername()) // Guardamos el email como "subject"
                .issuedAt(new Date(System.currentTimeMillis())) // Fecha de emisión
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration)) // Fecha de expiración (24h)
                .signWith(getSigningKey()) // Firma digital con nuestra clave secreta
                .compact(); // Convierte todo a la cadena final: xxx.yyy.zzz
    }

    /**
     * 2. EXTRAER EL USERNAME (Email):
     * Abre el token y extrae el "Subject" que guardamos al crearlo.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * 3. VALIDAR TOKEN:
     * Comprueba dos cosas:
     * a) Que el username dentro del token coincida con el usuario recibido.
     * b) Que el token NO haya expirado.
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    /**
     * Revisa si la fecha de expiración del token es anterior a la fecha actual.
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Extrae la fecha de expiración del token.
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Método genérico funcional:
     * Permite extraer cualquier Claim individual aplicando una función resolver sobre los Claims.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Abre el token sellado con la clave secreta y obtiene el Payload con todos sus Claims.
     * Si la firma fue manipulada o el formato es inválido, este método lanza una excepción criptográfica.
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey()) // Verifica que la firma coincida con la llave secreta
                .build()
                .parseSignedClaims(token)   // Abre el token sellado
                .getPayload();              // Retorna los Claims (datos)
    }

    /**
     * Convierte el String hexadecimal/Base64 de la clave secreta en un objeto SecretKey criptográfico.
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}

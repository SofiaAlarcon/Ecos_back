package com.semillero.ecosistema.util;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.semillero.ecosistema.entidad.Usuario;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;



@Service
public class JwtUtil {

    private String secret = "fsdfs46151@fde";

    public String generateToken(Usuario usuario) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("nombre", usuario.getNombre());
        claims.put("apellido", usuario.getApellido());
        claims.put("email", usuario.getEmail());
        claims.put("roles", usuario.getRol().name()); // Asumiendo que `getRol()` devuelve un enum o un valor que puede ser convertido a String
        claims.put("usuarioId", usuario.getId()); // Añadir el usuarioId a las reclamaciones

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(usuario.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 horas
                .signWith(SignatureAlgorithm.HS512, secret.getBytes(StandardCharsets.UTF_8))
                .compact();
    }
}
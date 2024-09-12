package com.semillero.ecosistema.controlador;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.semillero.ecosistema.dto.AuthResponse;
import com.semillero.ecosistema.entidad.Usuario;
import com.semillero.ecosistema.entidad.Usuario.RolDeUsuario;
import com.semillero.ecosistema.servicio.UsuarioServicioImpl;
import com.semillero.ecosistema.util.JwtUtil;


@RestController
@RequestMapping("/auth")
public class AuthControlador {

    @Autowired
    private UsuarioServicioImpl usuarioServicio;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateGoogleUser(@RequestBody Map<String, String> request) {
        try {
            String accessToken = request.get("accessToken");

            if (accessToken == null) {
                return ResponseEntity.badRequest().body("Missing access token");
            }

            // Realiza una solicitud a la API de Google para obtener la información del usuario
            String userInfoEndpoint = "https://www.googleapis.com/oauth2/v3/userinfo?access_token=" + accessToken;
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.getForEntity(userInfoEndpoint, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                // Analiza la respuesta JSON para obtener el correo electrónico
                ObjectMapper mapper = new ObjectMapper();
                JsonNode userJsonNode = mapper.readTree(response.getBody());
                String email = userJsonNode.get("email").asText();

                // Autentica al usuario en tu sistema
                Usuario usuario = usuarioServicio.authenticateUser(email);

                if (usuario == null) {
                    return ResponseEntity.status(401).body("Usuario no encontrado");
                }

                // Genera un JWT para el usuario autenticado
                String jwt = jwtUtil.generateToken(usuario);
                
             // Crear la cookie
                ResponseCookie cookie = ResponseCookie.from("jwt", jwt)
                        .httpOnly(true)
                        .secure(true)
                        .path("/")
                        .maxAge(36000)  // Duración en segundos
                        .build();
                
                AuthResponse authResponse = new AuthResponse(jwt);
                
                return ResponseEntity.ok()
                        .header(HttpHeaders.SET_COOKIE, cookie.toString())
                        .body(authResponse);
            } else {
                return ResponseEntity.status(response.getStatusCode()).body("Invalid access token");
            }
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid access token");
        }    
    }
    
    @PostMapping("/registro")
    public ResponseEntity<?> registerGoogleUser(@RequestBody Map<String, String> request) {
        try {
            String accessToken = request.get("accessToken");
            
            
            if (accessToken == null) {
                return ResponseEntity.badRequest().body("Missing access token");
            }

            // Realiza una solicitud a la API de Google para obtener la información del usuario
            String userInfoEndpoint = "https://www.googleapis.com/oauth2/v3/userinfo?access_token=" + accessToken;
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.getForEntity(userInfoEndpoint, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                // Analiza la respuesta JSON para obtener el correo electrónico
                ObjectMapper mapper = new ObjectMapper();
                JsonNode userJsonNode = mapper.readTree(response.getBody());
                String email = userJsonNode.get("email").asText();
                String nombre=userJsonNode.get("given_name").asText();
                String apellido=userJsonNode.get("family_name").asText();

                // Verifica si el usuario ya existe
                Usuario existe = usuarioServicio.authenticateUser(email);

                if (existe != null) {
                    return ResponseEntity.status(409).body("Usuario ya registrado");
                }

                // Registra al nuevo usuario
                Usuario nuevoUsuario = new Usuario();
                nuevoUsuario.setNombre(nombre);
                nuevoUsuario.setApellido(apellido);
                nuevoUsuario.setEmail(email);
                nuevoUsuario.setRol(RolDeUsuario.USUARIO);
                
                usuarioServicio.guardar(nuevoUsuario);

                // Genera un JWT para el usuario registrado
                String jwt = jwtUtil.generateToken(nuevoUsuario);
                
                // Crear la cookie
                ResponseCookie cookie = ResponseCookie.from("jwt", jwt)
                        .httpOnly(true)
                        .secure(true)
                        .path("/")
                        .maxAge(36000)  // Duración en segundos
                        .build();
                
                AuthResponse authResponse = new AuthResponse(jwt);

                return ResponseEntity.ok()
                        .header(HttpHeaders.SET_COOKIE, cookie.toString())
                        .body(authResponse);
            } else {
                return ResponseEntity.status(response.getStatusCode()).body("Invalid access token");
            }
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid access token");
        }    
    }
}
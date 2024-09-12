package com.semillero.ecosistema.configuracion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors().and() // Habilitar CORS usando la configuración proporcionada por CorsConfig
                .csrf().disable()
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/auth/login", "/auth/registro", "/pregunta").permitAll() // Permitir acceso libre a estos endpoints
                        .requestMatchers("/usuarios/**","/estadisticasProveedores","/proveedoresPorCategoria","/visualizaciones").hasRole("ADMIN") // Solo ADMIN puede acceder a desactivar usuarios
                        .requestMatchers("/publicar/**", "/editar-publicacion/**", "/borrar-publicacion/**", "/publicaciones","/nuevoProveedor","/editarEstado/**").hasRole("ADMIN") // Solo ADMIN puede publicar, editar y borrar publicaciones
                        .requestMatchers("/publicaciones/**", "/buscar/**").permitAll() // Permitir acceso a obtener publicaciones y buscar por ID a todos
                        .requestMatchers("/crearProveedor/**", "/editarProveedor/**", "misProveedores/**","/eliminarImagen/**","/actualizar/**").hasRole("USUARIO")
 //                       .requestMatchers("/buscarPorId/**").hasAnyRole("USUARIO","ADMIN")
                        .requestMatchers("/buscarPorCategoria/**", "/mostrarProveedorActivo","/mostrarTodo","/buscarPorId/**", "/proveedoresCercanos").permitAll()
                        .requestMatchers("/categorias/**", "/ubicacion/**","incrementarVisualizaciones/**").permitAll()

                        .requestMatchers("/error").anonymous() // Permitir acceso anónimo a /error
                        .anyRequest().authenticated() // Asegura que todas las demás solicitudes estén autenticadas
                )
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(userDetailsService), UsernamePasswordAuthenticationFilter.class);


        return http.build();
    }
}

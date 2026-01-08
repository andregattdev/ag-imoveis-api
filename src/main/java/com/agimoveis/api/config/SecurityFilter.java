package com.agimoveis.api.config;

import com.agimoveis.api.repository.UsuarioRepository;
import com.agimoveis.api.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository repository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();

        if (path.contains("/v3/api-docs") || path.contains("/swagger-ui")) {
        filterChain.doFilter(request, response);
        return;
    }

        var token = this.recoverToken(request);
        
        if (token != null) {
            var email = tokenService.validarToken(token);
            if (!email.isEmpty()) {
                var usuario = repository.findByEmail(email).orElseThrow();

                // Define a autoridade com o prefixo ROLE_ (ex: ROLE_ADMIN)
                var authority = new SimpleGrantedAuthority("ROLE_" + usuario.getRole().name());
                
                var authentication = new UsernamePasswordAuthenticationToken(usuario, null, List.of(authority));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null) return null;
        return authHeader.replace("Bearer ", "");
    }
}
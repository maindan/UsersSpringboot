package com.example.users.security.authentication;

import com.example.users.model.User;
import com.example.users.repositories.UserRepository;
import com.example.users.security.config.SecurityConfiguration;
import com.example.users.security.userDetail.UserDetailImplementation;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Component
public class UserAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if (checkIfEndpointIsNotPublic(request)) {
            String token = recoveryToken(request);
            if (token != null) {
                String subject = jwtTokenService.getSubjectFromToken(token);
                User user = userRepository.findByEmail(subject).get();
                UserDetailImplementation userDetails = new UserDetailImplementation(user);

                Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), null, userDetails.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                throw new RuntimeException("Token not found");
            }
        }
        filterChain.doFilter(request, response); // Continua o processamento da requisição
    }

    private String recoveryToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null) {
            return authorizationHeader.replace("Bearer ", "");
        }
        return null;
    }

    private boolean checkIfEndpointIsNotPublic(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String contextPath = request.getContextPath();

        System.out.println("Request URI: " + requestURI);
        System.out.println("Context Path: " + contextPath);

        boolean isPublic = Arrays.stream(SecurityConfiguration.ENDPOINTS_WITH_AUTH_NOT_REQUIRED)
                .anyMatch(publicEndpoint -> {
                    System.out.println("Comparando com: " + publicEndpoint);
                    return requestURI.startsWith(publicEndpoint);
                });

        System.out.println("É público? " + isPublic);

        return !isPublic; // Retorna TRUE se o endpoint não for público
    }
}

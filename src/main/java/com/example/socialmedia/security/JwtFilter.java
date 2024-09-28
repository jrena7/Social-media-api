package com.example.socialmedia.security;

import com.example.socialmedia.service.impl.CustomUserDetailsService;
import com.example.socialmedia.service.impl.InvalidTokenService;
import com.example.socialmedia.service.impl.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JWTService jwtService;

    @Autowired
    private InvalidTokenService invalidTokenService;

    @Autowired
    ApplicationContext applicationContext;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        // Get the token and username from the Authorization header
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);
            username = jwtService.extractUsername(token);
        }

        // If the username is not null and the user is not already authenticated
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Check if the token is invalid
            if (invalidTokenService.isInvalid(token)) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized access to resource");
                return;
            }

            // Load the user details from the database
            UserDetails userDetails = applicationContext.getBean(CustomUserDetailsService.class).loadUserByUsername(username);

            // Validate the token and set the authentication
            if (jwtService.validateToken(token, userDetails)) {

                // If details are valid, and token is valid, then check if the username in the path matches the username in the token
                if (!username.equals(extractUsernameFromPath(request))) {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized access to resource");
                    return;
                }

                UsernamePasswordAuthenticationToken upat =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                upat.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(upat);
            }
        }
        filterChain.doFilter(request, response);
    }

    // Helper method to extract the username from the URL path
    private String extractUsernameFromPath(HttpServletRequest request) {
        // Assuming the username is always the first path variable after the base URL (e.g., /{username}/resource)
        String[] pathParts = request.getRequestURI().split("/");
        if (pathParts.length > 4) {
            return pathParts[4];  // The username is the second part of the URL (after the base URL)
        }
        return null;
    }
}

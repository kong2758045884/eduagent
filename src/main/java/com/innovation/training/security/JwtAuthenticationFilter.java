package com.innovation.training.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String TOKEN_PREFIX = "Bearer ";

    private final JwtTokenProvider jwtTokenProvider;
    private final UserAuthDetailsService userAuthDetailsService;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider,
                                   UserAuthDetailsService userAuthDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userAuthDetailsService = userAuthDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String token = resolveToken(request);
        if (token != null && jwtTokenProvider.validateToken(token)
                && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                String username = jwtTokenProvider.getUsername(token);
                String teacherType = jwtTokenProvider.getTeacherType(token);
                UserDetails userDetails = userAuthDetailsService.loadUserByUsernameAndTeacherType(username, teacherType);

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (RuntimeException ex) {
                SecurityContextHolder.clearContext();
            }
        }

        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        if (authorization != null && authorization.startsWith(TOKEN_PREFIX)) {
            return authorization.substring(TOKEN_PREFIX.length());
        }
        return null;
    }
}

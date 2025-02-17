package com.app.us_twogether.security;

import com.app.us_twogether.domain.authentication.token.TokenBlacklistService;
import com.app.us_twogether.domain.authentication.token.TokenService;
import com.app.us_twogether.exception.DataNotFoundException;
import com.app.us_twogether.domain.user.UserRepository;
import com.app.us_twogether.exception.TokenBlacklistedException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    TokenService tokenService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TokenBlacklistService tokenBlacklistService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var token = this.recoverToken(request);

        if (token != null) {
            var username = tokenService.validateToken(token);

            if (tokenBlacklistService.isTokenBlacklisted(token)){
                throw new TokenBlacklistedException("Token inválido, faça login novamente.");
            }

            UserDetails user = userRepository.findByUsername(username).orElseThrow(() -> new DataNotFoundException("Usuário com token não foi encontrado"));

            var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request){
        var authHeader = request.getHeader("Authorization");

        if (authHeader == null)
            return null;

        return authHeader.replace("Bearer ",  "");
    }
}

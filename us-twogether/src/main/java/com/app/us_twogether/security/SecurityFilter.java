package com.app.us_twogether.security;

import com.app.us_twogether.domain.authentication.token.TokenService;
import com.app.us_twogether.exception.DataNotFoundException;
import com.app.us_twogether.domain.user.UserRepository;
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

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var token = this.recoverToken(request);

        if (token != null) {
            var username = tokenService.validateToken(token);
            UserDetails user = userRepository.findByUsername(username).orElseThrow(() -> new DataNotFoundException("Usuário com token não foi encontrado"));

            //faz a verificacoes das permissoes do usuario (token ativo)
            var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            //salva a autenticacao do usuario na requisicao para ser usada em outros contextos
            SecurityContextHolder.getContext().setAuthentication(authentication);

        }
        //vai para o proximo filtro, pois já finalizamos a validação do token
        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request){
        var authHeader = request.getHeader("Authorization");

        if (authHeader == null)
            return null;
        //substitui o codigo padrao "Bearer" para vazio, para pegar somente o token
        return authHeader.replace("Bearer ",  "");
    }
}

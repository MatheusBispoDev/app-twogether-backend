package com.app.us_twogether.security;

import com.app.us_twogether.domain.user.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret = "";

    public String generateToken(User user){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            return JWT.create().withIssuer("ustwogether-auth-api").withSubject(user.getUsername()).sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Error while generation token", exception);
        }
    }

    public String validateToken(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            return JWT.require(algorithm).withIssuer("ustwogether-auth-api").build().verify(token).getSubject();
        } catch (TokenExpiredException exception) {
            return "O token expirou. Por favor, faça login novamente. " + exception.getMessage();
        } catch (JWTVerificationException exception) {
            return "Erro na validação do token: " + exception.getMessage();
        } catch (JWTCreationException exception) {
            return "";
        }
    }
}

package com.app.us_twogether.domain.authentication.token;

import com.app.us_twogether.domain.authentication.AuthenticationMapper;
import com.app.us_twogether.domain.authentication.LoginResponseDTO;
import com.app.us_twogether.domain.user.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret = "";

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private AuthenticationMapper authenticationMapper;

    private final long refreshTokenValidity = 30L * 24 * 60 * 60;

    public LoginResponseDTO login(User user){
        String accessToken = generateToken(user.getUsername());
        RefreshToken refreshToken = createRefreshToken(user);

        return authenticationMapper.toLoginResponseDTO(user, accessToken, refreshToken);
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

    public void logout(String refreshToken) {
        RefreshToken token = findByToken(refreshToken);
        refreshTokenRepository.delete(token);
    }

    public RefreshToken findByToken(String token){
        return refreshTokenRepository.findByToken(token).orElseThrow(() -> new ResourceNotFoundException("Token não encontrado no Refresh"));
    }

    public boolean isTokenExpired(RefreshToken token) {
        return token.getExpiryDate().isBefore(Instant.now());
    }

    private String generateToken(String username){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            // 15 minutos
            long accessTokenValidity = 15 * 60 * 1000;
            return JWT.create().withIssuer("ustwogether-auth-api")
                    .withSubject(username)
                    .withIssuedAt(new Date())
                    .withExpiresAt(new Date(System.currentTimeMillis() + accessTokenValidity))
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Error while generation token", exception);
        }
    }

    private String generateRefreshToken(String username){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            // 30 dias
            return JWT.create()
                    .withIssuer("ustwogether-auth-api")
                    .withSubject(username)
                    .withIssuedAt(new Date())
                    .withExpiresAt(new Date(System.currentTimeMillis() + refreshTokenValidity * 1000))
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Error while generation token", exception);
        }
    }

    private RefreshToken createRefreshToken(User user){
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(generateRefreshToken(user.getUsername()));
        refreshToken.setExpiryDate(Instant.now().plusSeconds(refreshTokenValidity)); // 30 dias
        refreshToken.setUser(user);
        return refreshTokenRepository.save(refreshToken);
    }

}

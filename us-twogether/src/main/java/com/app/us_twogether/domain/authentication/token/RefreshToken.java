package com.app.us_twogether.domain.authentication.token;

import com.app.us_twogether.domain.user.User;
import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;

@Entity
@Table(name = "tb_refresh_tokens")
@Data
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long refreshTokenId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private Instant expiryDate;

    @Override
    public String toString() {
        return "RefreshToken{" +
                "refreshTokenId=" + refreshTokenId +
                ", token='" + token + '\'' +
                ", expiryDate=" + expiryDate +
                ", user=" + (user != null ? user.getUsername() : "null") +
                '}';
    }
}

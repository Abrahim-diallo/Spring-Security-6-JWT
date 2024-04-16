package fr.springsecurityjwtapp.spring_jwt_app.service;

import fr.springsecurityjwtapp.spring_jwt_app.dto.request.RefreshTokenRequest;
import fr.springsecurityjwtapp.spring_jwt_app.dto.response.RefreshTokenResponse;
import fr.springsecurityjwtapp.spring_jwt_app.entity.RefreshToken;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseCookie;

import java.util.Optional;

public interface RefreshTokenService {
    RefreshToken createRefreshToken(Integer userId);
    RefreshToken verifyExpiration(RefreshToken token);
    Optional<RefreshToken> findByToken(String token);
    RefreshTokenResponse generateNewToken(RefreshTokenRequest request);
    ResponseCookie generateRefreshTokenCookie(String token);
    String getRefreshTokenFromCookies(HttpServletRequest request);
    void deleteByToken(String token);
    ResponseCookie getCleanRefreshTokenCookie();
}

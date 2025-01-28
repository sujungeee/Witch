package com.ssafy.witch.jwt;

import static java.util.concurrent.TimeUnit.SECONDS;

import com.ssafy.witch.exception.auth.InvalidAccessTokenException;
import com.ssafy.witch.exception.auth.InvalidRefreshTokenException;
import com.ssafy.witch.exception.auth.RefreshTokenNotRenewableException;
import com.ssafy.witch.jwt.response.TokenResponse;
import com.ssafy.witch.port.RefreshTokenCachePort;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class JwtService {

  private static final String ACCESS_TOKEN_SUBJECT = "access-token";
  private static final String REFRESH_TOKEN_SUBJECT = "refresh-token";

  private static final String EMAIL_CLAIM = "email";
  private static final String ROLES_CLAIM = "roles";

  private final RefreshTokenCachePort refreshTokenCachePort;
  private final JwtProperties jwtProperties;

  public void validateAccessToken(String accessToken) {
    SecretKey key = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));

    try {
      Jwts.parser()
          .verifyWith(key)
          .build()
          .parseSignedClaims(accessToken);
    } catch (RuntimeException e) {
      throw new InvalidAccessTokenException();
    }
  }

  @Transactional
  public TokenResponse create(String email, List<String> roles) {
    String newAccessToken = createAccessToken(email, roles);
    String newRefreshToken = createRefreshToken(email, roles);

    refreshTokenCachePort.upsert(
        email,
        newRefreshToken,
        Duration.ofSeconds(jwtProperties.getRefreshTokenExpirationSeconds())
    );

    return TokenResponse.create(
        jwtProperties.getTokenType(),
        newAccessToken,
        jwtProperties.getAccessTokenExpirationSeconds(),
        newRefreshToken,
        jwtProperties.getRefreshTokenExpirationSeconds()
    );
  }

  @Transactional
  public TokenResponse reissue(String refreshToken) {
    Claims claims = validateRefreshToken(refreshToken);

    String email = claims.get(EMAIL_CLAIM, String.class);
    List<String> roles = ((List<?>) claims.get(ROLES_CLAIM))
        .stream()
        .map(Object::toString)
        .toList();

    String newAccessToken = createAccessToken(email, roles);

    return TokenResponse.refresh(
        jwtProperties.getTokenType(),
        newAccessToken,
        jwtProperties.getAccessTokenExpirationSeconds());
  }

  @Transactional
  public TokenResponse renew(String refreshToken) {
    Claims claims = validateRefreshToken(refreshToken);

    Date expiration = claims.getExpiration();
    long remainingMillis = expiration.getTime() - System.currentTimeMillis();

    if (remainingMillis > TimeUnit.SECONDS.toMillis(
        jwtProperties.getRefreshTokenExpirationSeconds())) {
      throw new RefreshTokenNotRenewableException();
    }

    String email = claims.get(EMAIL_CLAIM, String.class);
    List<String> roles = ((List<?>) claims.get(ROLES_CLAIM))
        .stream()
        .map(Object::toString)
        .toList();

    String newAccessToken = createAccessToken(email, roles);
    String newRefreshToken = createRefreshToken(email, roles);

    refreshTokenCachePort.upsert(
        email,
        newRefreshToken,
        Duration.ofSeconds(jwtProperties.getRefreshTokenExpirationSeconds())
    );

    return TokenResponse.create(
        jwtProperties.getTokenType(),
        newAccessToken,
        jwtProperties.getAccessTokenExpirationSeconds(),
        newRefreshToken,
        jwtProperties.getRefreshTokenExpirationSeconds()
    );
  }

  // 공통 리프레시 토큰 검증 로직
  private Claims validateRefreshToken(String refreshToken) {
    SecretKey key = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));
    Claims claims = Jwts.parser()
        .verifyWith(key)
        .build()
        .parseSignedClaims(refreshToken)
        .getPayload();

    String email = claims.get(EMAIL_CLAIM, String.class);
    String cachedToken = refreshTokenCachePort.get(email)
        .orElseThrow(InvalidRefreshTokenException::new);

    if (!cachedToken.equals(refreshToken)) {
      throw new InvalidRefreshTokenException();
    }

    return claims;
  }

  private String createAccessToken(String email, List<String> roles) {
    return buildToken(
        email,
        roles,
        ACCESS_TOKEN_SUBJECT,
        jwtProperties.getAccessTokenExpirationSeconds()
    );
  }

  private String createRefreshToken(String email, List<String> roles) {
    return buildToken(
        email,
        roles,
        REFRESH_TOKEN_SUBJECT,
        jwtProperties.getRefreshTokenExpirationSeconds()
    );
  }

  private String buildToken(String email, List<String> roles, String subject,
      Long expirationSeconds) {
    SecretKey key = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));

    return Jwts.builder()
        .subject(subject)
        .claim(EMAIL_CLAIM, email)
        .claim(ROLES_CLAIM, roles)
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(
            new Date(System.currentTimeMillis() + SECONDS.toMillis(expirationSeconds)))
        .signWith(key)
        .compact();
  }
}

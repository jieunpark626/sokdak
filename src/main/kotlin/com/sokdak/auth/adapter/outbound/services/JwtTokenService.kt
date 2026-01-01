package com.sokdak.auth.adapter.outbound.services

import com.sokdak.auth.application.exceptions.InvalidTokenException
import com.sokdak.auth.application.exceptions.TokenExpiredException
import com.sokdak.auth.domain.services.TokenService
import com.sokdak.auth.domain.valueobjects.AuthTokens
import com.sokdak.auth.domain.valueobjects.UserId
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.security.SignatureException
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.Date
import java.util.concurrent.TimeUnit

@Service
class JwtTokenService(
    @Value("\${jwt.secret}") private val secret: String,
    @Value("\${jwt.access-token-validity}") private val accessTokenValidityMs: Long,
    @Value("\${jwt.refresh-token-validity}") private val refreshTokenValidityMs: Long,
) : TokenService {
    private val key = run {
        require(secret.length >= 32) {
            "JWT secret must be at least 32 characters (256 bits)"
        }
        Keys.hmacShaKeyFor(secret.toByteArray())
    }

    override fun generateTokens(userId: UserId): AuthTokens {
        val now = Instant.now()
        val accessTokenExpiry = now.plusMillis(accessTokenValidityMs)
        val refreshTokenExpiry = now.plusMillis(refreshTokenValidityMs)

        val accessToken =
            Jwts
                .builder()
                .subject(userId.value)
                .issuedAt(Date.from(now))
                .expiration(Date.from(accessTokenExpiry))
                .signWith(key)
                .compact()

        val refreshToken =
            Jwts
                .builder()
                .subject(userId.value)
                .issuedAt(Date.from(now))
                .expiration(Date.from(refreshTokenExpiry))
                .claim("type", "refresh")
                .signWith(key)
                .compact()

        return AuthTokens(
            accessToken = accessToken,
            refreshToken = refreshToken,
            expiresInSeconds = TimeUnit.MILLISECONDS.toSeconds(accessTokenValidityMs),
        )
    }

    override fun validateToken(token: String): UserId {
        try {
            val claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .payload

            if (claims["type"] == "refresh") {
                throw InvalidTokenException("Refresh token cannot be used for authentication")
            }

            return UserId(claims.subject)

        } catch (e: ExpiredJwtException) {
            throw TokenExpiredException("Token has expired")
        } catch (e: MalformedJwtException) {
            throw InvalidTokenException("Malformed JWT token")
        } catch (e: SignatureException) {
            throw InvalidTokenException("Invalid JWT signature")
        } catch (e: IllegalArgumentException) {
            throw InvalidTokenException("JWT token is empty or invalid")
        }
    }
}

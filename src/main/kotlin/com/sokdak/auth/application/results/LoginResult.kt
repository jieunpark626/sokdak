package com.sokdak.auth.application.results

// TODO: USER 정보 뭘 줘야할 지
// 1. 사용자 정보
data class LoginUserResult(
    val userId: String,
    val name: String
)

// 2. 토큰 정보
data class TokenResult(
    val accessToken: String,
    val refreshToken: String,
    val tokenType: String = "Bearer",
    val expiresInSeconds: Long,
)

// 3. 전체 로그인 응답
data class LoginResult(
    val user: LoginUserResult,
    val token: TokenResult,
)

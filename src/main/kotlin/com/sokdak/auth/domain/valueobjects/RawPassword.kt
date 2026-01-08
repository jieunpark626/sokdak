package com.sokdak.auth.domain.valueobjects

@JvmInline
value class RawPassword private constructor(val value: String) {
    companion object {
        // 회원가입용: 비밀번호 형식 검증 수행
        fun of(password: String): RawPassword {
            require(password.length >= 8) {
                "Password must be at least 8 characters"
            }
            require(password.any { it.isUpperCase() }) {
                "Password must contain at least one uppercase letter"
            }
            require(password.any { it.isLowerCase() }) {
                "Password must contain at least one lowercase letter"
            }
            require(password.any { it.isDigit() }) {
                "Password must contain at least one digit"
            }
            return RawPassword(password)
        }

        // 로그인용: 검증 없이 생성 (이미 저장된 비밀번호와 비교만 수행)
        fun forLogin(password: String): RawPassword = RawPassword(password)
    }
}

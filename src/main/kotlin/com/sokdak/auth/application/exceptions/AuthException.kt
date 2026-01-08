package com.sokdak.auth.application.exceptions

sealed class AuthException(message: String) : RuntimeException(message)

class DuplicateLoginIdException(message: String) : AuthException(message)

class DuplicateEmailException(message: String) : AuthException(message)

class InvalidPasswordException(message: String) : AuthException(message)

class UserNotFoundException(message: String) : AuthException(message)

class InvalidCredentialsException(message: String) : AuthException(message)

class InvalidTokenException(message: String) : AuthException(message)

class TokenExpiredException(message: String) : AuthException(message)

class EmailNotVerifiedException(message: String) : AuthException(message)

class VerificationTokenNotFoundException(message: String) : AuthException(message)

class VerificationTokenExpiredException(message: String) : AuthException(message)

class EmailAlreadyVerifiedException(message: String) : AuthException(message)

class EmailSendFailedException(message: String) : AuthException(message)

package com.sokdak.auth.application.exceptions


sealed class AuthException(message: String) : RuntimeException(message)

class DuplicateLoginIdException(message: String) : AuthException(message)

class DuplicateEmailException(message: String) : AuthException(message)

class InvalidPasswordException(message: String) : AuthException(message)

class UserNotFoundException(message: String) : AuthException(message)

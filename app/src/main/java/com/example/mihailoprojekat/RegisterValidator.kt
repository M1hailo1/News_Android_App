package com.example.mihailoprojekat

class RegisterValidator {
    fun isEmailValid(email: String): Boolean {
        return email.contains('@') && email.contains('.')
    }

    fun isPasswordValid(password: String): Boolean {
        return password.length >= 6
    }
}
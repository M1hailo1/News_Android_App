package com.example.mihailoprojekat

import org.junit.Test
import org.junit.Assert.*
import org.junit.Before

class ExampleUnitTest {

    private lateinit var validator: RegisterValidator

    @Before
    fun setup() {
        validator = RegisterValidator()
    }

    @Test
    fun email_is_valid() {
        val result = validator.isEmailValid("mihailomaricisc@gmail.com")
        assertTrue(result)
    }

    @Test
    fun email_is_invalid_no_at_symbol() {
        val result = validator.isEmailValid("mihailomaricicgmailcom")
        assertFalse(result)
    }

    @Test
    fun email_is_invalid_no_dot() {
        val result = validator.isEmailValid("mihailo@gmailcom")
        assertFalse(result)
    }

    @Test
    fun email_is_invalid_empty() {
        val result = validator.isEmailValid("")
        assertFalse(result)
    }

    @Test
    fun password_is_valid() {
        val result = validator.isPasswordValid("123456")
        assertTrue(result)
    }

    @Test
    fun password_is_valid_longer() {
        val result = validator.isPasswordValid("verylongpassword123")
        assertTrue(result)
    }

    @Test
    fun password_is_invalid_too_short() {
        val result = validator.isPasswordValid("1234")
        assertFalse(result)
    }

    @Test
    fun password_is_invalid_empty() {
        val result = validator.isPasswordValid("")
        assertFalse(result)
    }

    @Test
    fun password_exactly_six_characters() {
        val result = validator.isPasswordValid("abcdef")
        assertTrue(result)
    }
}
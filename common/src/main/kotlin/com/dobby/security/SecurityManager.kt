package com.dobby.security

interface SecurityManager {
    fun parseAuthentication(token: String): SecurityUser
}

package com.example.cardmanager.utils

import java.security.MessageDigest

fun hashString(input: String): String {
    val bytes = input.toByteArray()
    val digest = MessageDigest.getInstance("SHA-256").digest(bytes)
    return digest.joinToString("") { "%02x".format(it) }
}

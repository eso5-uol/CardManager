package com.example.cardmanager.utils.exceptions

import org.springframework.http.HttpStatus
import java.time.LocalDateTime

data class Error (
    val message: String,
    val status: HttpStatus,
    val code: Int,
    val timestamp: LocalDateTime
)

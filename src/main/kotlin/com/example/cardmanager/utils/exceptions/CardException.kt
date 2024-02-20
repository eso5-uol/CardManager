package com.example.cardmanager.utils.exceptions

import org.springframework.http.HttpStatus

sealed class CardException (message: String, val httpStatus: HttpStatus): Throwable(message) {
    class CardNotFoundException(message: String = "Card not found") : CardException(message, HttpStatus.NOT_FOUND)
    class InsufficientBalanceException(message: String = "Insufficient funds") : CardException(message, HttpStatus.BAD_REQUEST)
}

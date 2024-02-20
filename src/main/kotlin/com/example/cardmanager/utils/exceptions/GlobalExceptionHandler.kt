package com.example.cardmanager.utils.exceptions

import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import org.springframework.web.servlet.NoHandlerFoundException
import java.rmi.UnexpectedException
import java.time.LocalDateTime


@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(CardException.CardNotFoundException::class)
    fun handleCardNotFoundException(ex: CardException.CardNotFoundException): ResponseEntity<Error> {
        val errorMessage = "Error occurred while retrieving card: ${ex.message}"
        val error = Error(
            message = errorMessage,
            status = HttpStatus.NOT_FOUND,
            code = HttpStatus.NOT_FOUND.value(),
            timestamp = LocalDateTime.now()
        )
        return ResponseEntity(error, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(CardException.InsufficientBalanceException::class)
    fun handleInsufficientBalanceException(ex: CardException.InsufficientBalanceException): ResponseEntity<Error> {
        val error = Error(
            message = ex.message ?: "Insufficient balance",
            status = HttpStatus.BAD_REQUEST,
            code = HttpStatus.BAD_REQUEST.value(),
            timestamp = LocalDateTime.now()
        )
        return ResponseEntity(error, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(NoHandlerFoundException::class)
    fun handleNotFoundException(ex: NoHandlerFoundException): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Route not found")
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun handleRequestPathVariableValidationException(ex: MethodArgumentTypeMismatchException): ResponseEntity<Error> {
        val error = Error(
            message = ex.message ?: "Path variable validation failed",
            status = HttpStatus.BAD_REQUEST,
            code = HttpStatus.BAD_REQUEST.value(),
            timestamp = LocalDateTime.now()
        )
        return ResponseEntity(error, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(DataIntegrityViolationException::class)
    fun handleDataIntegrityViolationException(ex: DataIntegrityViolationException): ResponseEntity<Any> {
        val errorMessage = if (ex.message?.contains("account_number_hash") == true) {
            "Duplicate account number"
        } else {
            "Data integrity violation"
        }
        val error = Error(
            message = errorMessage,
            status = HttpStatus.CONFLICT,
            code = HttpStatus.CONFLICT.value(),
            timestamp = LocalDateTime.now()
        )
        return ResponseEntity(error, HttpStatus.CONFLICT)
    }

    @ExceptionHandler(UnexpectedException::class)
    fun handleUnexpectedException(ex: Exception): ResponseEntity<Error> {
        val errorMessage = "An unexpected error occurred: ${ex.message}"
        val error = Error(
            message = errorMessage,
            status = HttpStatus.INTERNAL_SERVER_ERROR,
            code = HttpStatus.INTERNAL_SERVER_ERROR.value(),
            timestamp = LocalDateTime.now()
        )
        return ResponseEntity(error, HttpStatus.INTERNAL_SERVER_ERROR)
    }
}

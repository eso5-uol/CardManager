package com.example.cardmanager.model

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.PositiveOrZero
import java.math.BigDecimal

data class CreateCardRequest (
    @field:NotBlank
    val firstName: String,
    @field:NotBlank
    val lastName: String,
    @field:NotBlank
    val scheme: Scheme,
    @field:NotBlank
    @field: PositiveOrZero
    val creditLimit: BigDecimal,
    @field:NotBlank
    val accountNumber: String,
)

data class UpdateCardRequest (
    @field:NotBlank
    @field: PositiveOrZero
    val creditLimit: BigDecimal,
)

data class DebitCardRequest (
    @field:NotBlank
    @field: PositiveOrZero
    val amount: BigDecimal,
    @field:NotBlank
    val transactionType: TransactionType = TransactionType.DEBIT
)

data class CreditCardRequest (
    @field:NotBlank
    @field: PositiveOrZero
    val amount: BigDecimal,
    @field:NotBlank
    val transactionType: TransactionType = TransactionType.CREDIT
)

enum class TransactionType {
    DEBIT, CREDIT
}

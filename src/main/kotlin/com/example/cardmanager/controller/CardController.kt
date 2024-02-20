package com.example.cardmanager.controller

import com.example.cardmanager.model.*
import com.example.cardmanager.service.CardService
import com.example.cardmanager.utils.validators.AccountLengthValidator
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/credit-cards")
class CardController(private val cardService: CardService, private val accountLengthValidator: AccountLengthValidator) {

    @GetMapping
    fun getAllCards(): ResponseEntity<Collection<Card>> {
        val cards = cardService.listCards()
        return ResponseEntity.ok().body(cards)
    }

    @GetMapping("/{accountNumber}")
    fun getCardByAccountNumber(@PathVariable accountNumber: String): ResponseEntity<Card> {
        accountLengthValidator.validateAccountNumber(accountNumber)
        return ResponseEntity.ok().body(cardService.getCardByAccountNumberHash(accountNumber))
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createCard(@Valid @RequestBody card: CreateCardRequest): ResponseEntity<Card> {
        return ResponseEntity.status(HttpStatus.CREATED).body(cardService.createCard(card))
    }

    @PutMapping("/{accountNumber}")
    fun updateResource(
        @PathVariable accountNumber: String,
        @RequestBody updateCardParams: UpdateCardRequest
    ): ResponseEntity<Card> {
        accountLengthValidator.validateAccountNumber(accountNumber)
        val updatedCard = cardService.updateCreditLimit(accountNumber, updateCardParams)
        return ResponseEntity.ok().body(updatedCard)
    }

    @DeleteMapping("/{accountNumber}")
    fun deleteResource(@PathVariable accountNumber: String) {
        accountLengthValidator.validateAccountNumber(accountNumber)
        cardService.deleteCard(accountNumber)
    }


    @PostMapping("{accountNumber}/credit")
    fun creditCard(
        @Valid @PathVariable accountNumber: String,
        @RequestBody cardTransactionParams: CreditCardRequest
    ): ResponseEntity<Card> {
        accountLengthValidator.validateAccountNumber(accountNumber)

        val updatedCard = cardService.creditCard(accountNumber, cardTransactionParams)
        return ResponseEntity.ok().body(updatedCard)
    }

    @PostMapping("{accountNumber}/charge")
    fun debitCard(
        @Valid @PathVariable accountNumber: String,
        @RequestBody cardTransactionParams: DebitCardRequest
    ): ResponseEntity<Card> {
        accountLengthValidator.validateAccountNumber(accountNumber)

        val updatedCard = cardService.debitCard(accountNumber, cardTransactionParams)
        return ResponseEntity.ok().body(updatedCard)
    }

}


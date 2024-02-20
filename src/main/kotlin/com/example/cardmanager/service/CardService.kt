package com.example.cardmanager.service

import com.example.cardmanager.model.*
import com.example.cardmanager.repository.DefaultCardRepository
import com.example.cardmanager.utils.exceptions.CardException
import com.example.cardmanager.utils.hashString
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDateTime
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.transaction.annotation.Transactional

@Service
class CardService(private val cardRepository: DefaultCardRepository) {
    private val logger: Logger = LoggerFactory.getLogger(CardService::class.java)

    fun listCards(): List<Card> {
        logger.info("Retrieving list of cards")
        return cardRepository.findAll()
    }

    fun getCardByAccountNumberHash(accountNumber: String): Card {
        logger.info("Retrieving card buy account number hash")
        val acctNumberHash = hashString(accountNumber)
        val card = cardRepository.findByAccountNumberHash(acctNumberHash) ?: throw CardException.CardNotFoundException()
        return card
    }


    fun createCard(createCardParams: CreateCardRequest): Card {
        logger.info("Creating card for account holder: ${createCardParams.firstName} ${createCardParams.lastName}")
        val card = Card(
            firstName = createCardParams.firstName,
            lastName = createCardParams.lastName,
            accountNumberHash = hashString(createCardParams.accountNumber),
            creditLimit = createCardParams.creditLimit,
            balance = createCardParams.creditLimit,
            scheme = createCardParams.scheme,
            created = LocalDateTime.now(),
            updated = LocalDateTime.now(),
        )
        val savedCard = cardRepository.save(card);
        return savedCard
    }

    @Transactional
    open fun updateCreditLimit(accountNumber: String, updateCardParams: UpdateCardRequest): Card {
        val acctNumberHash = hashString(accountNumber)
        val card = cardRepository.findByAccountNumberHash(acctNumberHash) ?: throw CardException.CardNotFoundException()
        val newBalance = calculateNewBalance(card.balance, card.creditLimit, updateCardParams.creditLimit)

        val updatedCard = card.copy(
            creditLimit = updateCardParams.creditLimit,
            balance = newBalance,
            updated = LocalDateTime.now(),
        )

        return cardRepository.save(updatedCard)

    }

    private fun calculateNewBalance(
        balance: BigDecimal,
        oldCreditLimit: BigDecimal,
        newCreditLimit: BigDecimal,
    ): BigDecimal {
        val creditDelta = newCreditLimit.subtract(oldCreditLimit)
        return if (creditDelta > BigDecimal.ZERO) {
            balance.add(creditDelta)
        } else {
            newCreditLimit.min(balance)
        }
    }

    fun deleteCard(accountNumber: String) {
        val acctNumberHash = hashString(accountNumber)
        val card = cardRepository.findByAccountNumberHash(acctNumberHash) ?: throw CardException.CardNotFoundException()
        cardRepository.delete(card)
    }

    @Transactional
    open fun creditCard(accountNumber: String, creditCardParams: CreditCardRequest): Card {
        val acctNumberHash = hashString(accountNumber)

        val card = cardRepository.findByAccountNumberHash(acctNumberHash) ?: throw CardException.CardNotFoundException()
        val updatedCard = card.copy(
            balance = card.balance + creditCardParams.amount,
            updated = LocalDateTime.now()
        )

        return cardRepository.save(updatedCard);
    }

    @Transactional
    open fun debitCard(accountNumber: String, debitCardParams: DebitCardRequest): Card {
        val acctNumberHash = hashString(accountNumber)
        val card = cardRepository.findByAccountNumberHash(acctNumberHash) ?: throw CardException.CardNotFoundException()

        if (card.balance < debitCardParams.amount) {
            throw CardException.InsufficientBalanceException("Insufficient balance for account ID: ${card.id}")
        }

        val updatedCard = card.copy(
            balance = card.balance - debitCardParams.amount,
            updated = LocalDateTime.now()
        )
        return cardRepository.save(updatedCard)

    }
}

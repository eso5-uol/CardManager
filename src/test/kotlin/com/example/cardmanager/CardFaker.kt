package com.example.cardmanager

import com.example.cardmanager.model.Card
import com.example.cardmanager.model.Scheme
import com.example.cardmanager.utils.hashString
import com.github.javafaker.Faker
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

class CardFaker {
    private val faker: Faker = Faker()

    fun generateCards(noOfCards: Int): List<Card> {
        return (1..noOfCards).map {
            Card(
                id = UUID.randomUUID(),
                firstName = faker.name().firstName(),
                lastName = faker.name().lastName(),
                accountNumberHash = hashString(faker.finance().creditCard()),
                creditLimit = BigDecimal.valueOf(faker.number().randomNumber()),
                balance = BigDecimal.valueOf(faker.number().randomNumber()),
                scheme = Scheme.VISA,
                created = LocalDateTime.now(),
                updated = LocalDateTime.now(),
            )
        }
    }

    fun generateCardWithOverride(override: Map<String, Any> = mapOf()): Card {
        return Card(
            id = override["id"] as? UUID ?: UUID.randomUUID(),
            firstName = faker.name().firstName(),
            lastName = faker.name().lastName(),
            accountNumberHash = override["accountNumberHash"] as? String ?: hashString(faker.finance().creditCard()),
            creditLimit = BigDecimal.valueOf(faker.number().randomNumber()),
            balance = BigDecimal.valueOf(faker.number().randomNumber()),
            scheme = Scheme.VISA,
            created = LocalDateTime.now(),
            updated = LocalDateTime.now(),
        )
    }


}

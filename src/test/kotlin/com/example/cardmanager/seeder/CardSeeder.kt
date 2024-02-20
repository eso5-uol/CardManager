package com.example.cardmanager.seeder

import com.example.cardmanager.model.Card
import com.example.cardmanager.model.Scheme
import com.example.cardmanager.repository.DefaultCardRepository
import com.example.cardmanager.utils.hashString
import com.github.javafaker.Faker
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

@Component
class CardSeeder(private val cardRepository: DefaultCardRepository): ApplicationRunner {
    override fun run(args: ApplicationArguments) {
        val faker = Faker()
        for ( i in 1..5) {
            val id = UUID.randomUUID()
            val firstName = faker.name().firstName()
            val lastName = faker.name().lastName()
            val accountNumberHash = hashString(faker.finance().creditCard())
            val creditLimit = BigDecimal.valueOf(faker.number().randomNumber())
            val balance = creditLimit - BigDecimal.valueOf(1)
            val scheme = Scheme.VISA
            val created = LocalDateTime.now()
            val updated = LocalDateTime.now()
            val card = Card(
                id, firstName, lastName, scheme, accountNumberHash, creditLimit, balance, created, updated)

            this.cardRepository.save(card)
        }
    }
}

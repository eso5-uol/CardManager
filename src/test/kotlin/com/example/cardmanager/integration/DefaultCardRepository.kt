package com.example.cardmanager.integration

import com.example.cardmanager.model.Card
import com.example.cardmanager.model.Scheme
import com.example.cardmanager.repository.DefaultCardRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.Rollback
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.LocalDateTime

@SpringBootTest
@Transactional
class DefaultCardRepositoryIntegrationTest {

    @Autowired
    private lateinit var repository: DefaultCardRepository

    @BeforeEach
    fun setup() {
        repository.deleteAll()
    }

    @Test
    @Rollback
    fun testFindByAccountNumberHash() {
        // Save a sample card to the database
        val savedCard = repository.save(
            Card(null, "John", "Doe", Scheme.VISA, "1234567890", BigDecimal.TEN, BigDecimal.ZERO, LocalDateTime.now(), LocalDateTime.now()))

        // Retrieve the card from the database using the repository
        val result = repository.findByAccountNumberHash("1234567890")
        println("Account number hash result find is:  $result")

        // Assert that the card returned by the repository matches the saved card
        assertEquals(savedCard, result)
    }
}

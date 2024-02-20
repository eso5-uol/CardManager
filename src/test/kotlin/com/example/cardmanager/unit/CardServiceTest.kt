package com.example.cardmanager.unit

import com.example.cardmanager.repository.DefaultCardRepository
import com.example.cardmanager.service.CardService
import org.junit.jupiter.api.Test
import org.springframework.boot.test.mock.mockito.MockBean

class CardServiceTest {
    @MockBean
    private lateinit var repository: DefaultCardRepository

    private val cardService = CardService(repository)

    @Test
    fun `should call card data source`() {
        cardService.listCards()
    }
}

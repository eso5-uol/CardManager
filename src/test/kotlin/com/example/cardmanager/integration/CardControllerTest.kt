package com.example.cardmanager.integration

import com.example.cardmanager.CardFaker
import com.example.cardmanager.model.Card
import com.example.cardmanager.model.Scheme
import com.example.cardmanager.repository.DefaultCardRepository
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.javafaker.Faker
import org.json.JSONArray
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.*
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID
import org.junit.jupiter.api.Assertions.assertEquals

@SpringBootTest()
@AutoConfigureMockMvc
internal class CardControllerTest @Autowired constructor (
    var mockMvc: MockMvc,
    var objectMapper: ObjectMapper
) {

    @MockBean
    private lateinit var repository: DefaultCardRepository

    private val faker: Faker = Faker()
    private val cardFaker = CardFaker()

    val baseUrl = "/api/credit-cards"

    @Nested
    @DisplayName("GET /api/credit-cards")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class GetCards {
        @Test
        fun `should return all cards`() {

            val uuid = UUID.randomUUID()
            // Mock the data to be returned by the repository
            val card1 =  cardFaker.generateCardWithOverride(mapOf("id" to uuid))
            val card2 =  cardFaker.generateCards(1)[0]
            val cards = listOf(card1, card2)
            `when`(repository.findAll()).thenReturn(cards)

            val getAllRequest = mockMvc.get(baseUrl)
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    jsonPath("$[0].id") { value(uuid.toString()) }
                }.andReturn()

            val responseBody = getAllRequest.response.contentAsString
            val jsonArray = JSONArray(responseBody) // Import org.json.JSONArray
            assertEquals(cards.size, jsonArray.length())
        }
    }

    @Nested
    @DisplayName("GET /api/credit-cards/{accountNumber}")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class GetCardByAccountNumber {

        @Test
        fun `should return an error if account number is invalid`() {
            val accountNumber = "123456"
            val getRequest = mockMvc.get("$baseUrl/$accountNumber")

            getRequest
                .andDo { print() }
                .andExpect {
                    status { isBadRequest() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                }

        }

        @Test
        fun `should return a NOT_FOUND error if account number is not found`() {
            val accountNumber = faker.business().creditCardNumber()
            val getRequest = mockMvc.get("$baseUrl/$accountNumber")

            getRequest
                .andDo { print() }
                .andExpect {
                    status { isNotFound() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                }

        }

        @Test
        fun `should return a single card by account number`() {
            val accountNumber = "123456"
            val getRequest = mockMvc.get("$baseUrl/$accountNumber")

            getRequest
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    jsonPath("$.accountNumber") { value(accountNumber) }
                }

        }
    }

    @Nested
    @DisplayName("POST /api/credit-cards")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class CreateNewCard {
        @Test
        fun `should create a new card`() {
            val newCard = Card(
                firstName = "John",
                lastName = "Doe",
                accountNumberHash = "123456",
                creditLimit = BigDecimal.valueOf(1000.00),
                balance = BigDecimal.valueOf(1000.00),
                scheme = Scheme.VISA,
                created = LocalDateTime.now(),
                updated = LocalDateTime.now()
            )

            val postRequest = mockMvc.post(baseUrl) {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(newCard)
            }

            postRequest
                .andDo { print() }
                .andExpect {
                    status { isCreated() }
                    content {
                        contentType(MediaType.APPLICATION_JSON)
                        json(objectMapper.writeValueAsString(newCard)) }
                    jsonPath("$.id") { value("expected value") }
                }


        }
    }

    @Nested
    @DisplayName("PUT /api/credit-cards/{id}")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class UpdateCard {
        @Test
        fun `should update a card`() {
            val updatedCard = Card(
                firstName = "John",
                lastName = "Doe",
                accountNumberHash = "123456",
                creditLimit = BigDecimal.valueOf(1000.00),
                balance = BigDecimal.valueOf(1000.00),
                scheme = Scheme.VISA,
                created = LocalDateTime.now(),
                updated = LocalDateTime.now()
            )

            val performPutRequest = mockMvc.put("$baseUrl/{id}", 1) {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(updatedCard)
            }

            performPutRequest
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    content { contentType(MediaType.APPLICATION_JSON)
                        json(objectMapper.writeValueAsString(updatedCard))
                    }
                }

            mockMvc.get("$baseUrl/123456", 1)
                .andExpect {
                    status { isOk() }
                    content { contentType(MediaType.APPLICATION_JSON)
                        json(objectMapper.writeValueAsString(updatedCard))
                    }
                }
        }
    }

    @Nested
    @DisplayName("DELETE /api/credit-cards/{id}")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class DeleteCard {
        @Test
        fun `should delete a card`() {
            mockMvc.delete("$baseUrl/123456")
                .andDo { print() }
                .andExpect { status { isNoContent() } }
        }

        @Test
        fun `should return NOT_FOUND if bank with account number does not exist`() {
            mockMvc.delete("$baseUrl/123456")
                .andDo { print() }
                .andExpect { status { isNotFound() } }
        }
    }
}




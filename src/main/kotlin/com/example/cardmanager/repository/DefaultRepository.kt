package com.example.cardmanager.repository

import com.example.cardmanager.model.Card
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface DefaultCardRepository: JpaRepository<Card, UUID> {
    fun findByAccountNumberHash(accountNumberHash: String): Card?
}


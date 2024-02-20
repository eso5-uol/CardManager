package com.example.cardmanager.model

import jakarta.persistence.*
import org.hibernate.annotations.GenericGenerator
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "CARDS", indexes = [Index(name = "card_by_account_number_hash", columnList = "account_number_hash", unique = true)])
data class Card(
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
        name = "UUID",
        strategy = "org.hibernate.id.UUIDGenerator"
    )
    val id: UUID? = null,

    @Column(name = "first_name", nullable = false)
    val firstName: String,

    @Column(name = "last_name", nullable = false)
    val lastName: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val scheme: Scheme,

    @Column(name = "account_number_hash", nullable = false, unique = true)
    val accountNumberHash: String,

    @Column(name = "credit_limit", nullable = false)
    val creditLimit: BigDecimal,

    @Column(nullable = false)
    val balance: BigDecimal,

    @Column(nullable = false)
    val created: LocalDateTime,

    @Column(nullable = false)
    val updated: LocalDateTime
) {
    constructor() : this(UUID.randomUUID(), "", "", Scheme.VISA, "", BigDecimal.ZERO, BigDecimal.ZERO, LocalDateTime.MIN, LocalDateTime.MIN)
}

enum class Scheme {
    VISA,
    MASTERCARD,
}

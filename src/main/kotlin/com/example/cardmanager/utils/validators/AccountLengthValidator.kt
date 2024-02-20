package com.example.cardmanager.utils.validators

import org.springframework.stereotype.Component

@Component
class AccountLengthValidator{
    @Throws(IllegalArgumentException::class)
    fun validateAccountNumber(value: String?) {
        if (value.isNullOrEmpty() || (value.length != 13 && value.length != 16)) {
            throw IllegalArgumentException("Account number must be 13 or 16 characters long")
        }
    }

}

package com.example.cardmanager

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class CardManagerApplication


fun main(args: Array<String>) {
	runApplication<CardManagerApplication>(*args)
}

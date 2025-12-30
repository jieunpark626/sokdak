package com.sokdak

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
class SokdakCoreApplication

fun main(args: Array<String>) {
    runApplication<com.sokdak.SokdakCoreApplication>(*args)
}

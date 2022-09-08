package com.karapirinc.beetlejuice

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.karapirinc.beetlejuice.user.model.User
import com.karapirinc.beetlejuice.user.repository.UserRepository
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import java.util.*

@SpringBootApplication
class BeetlejuiceApplication

fun main(args: Array<String>) {

    runApplication<BeetlejuiceApplication>(*args)
}

@Bean
@Primary
fun objectMapper(): ObjectMapper {
    return ObjectMapper().registerModule(JavaTimeModule()).registerModule(Jdk8Module())
        .registerModule(KotlinModule.Builder().build()).configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true)
}


@Component
class UserRunner(private val userRepository: UserRepository) : ApplicationRunner {


    override fun run(args: ApplicationArguments?) {
        val userIds = Flux.just(
            User(UUID.fromString("5f390628-ba5b-4160-814b-11d2edc8e24b")),
            User(UUID.fromString("b9b3b54b-22de-41d2-9328-075e8353a0f1")),
            User(UUID.fromString("6ee1e4dc-59a4-4b27-848d-6ed5294d5acf"))
        ).flatMap { userRepository.save(it) }

        //TODO private val LOG = KotlinLogging.logger {}
        userIds.subscribe { println(it) }

    }

}
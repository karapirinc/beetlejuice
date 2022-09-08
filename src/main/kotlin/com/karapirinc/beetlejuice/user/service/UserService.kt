package com.karapirinc.beetlejuice.user.service

import com.karapirinc.beetlejuice.user.exception.UserNotFoundException
import com.karapirinc.beetlejuice.user.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Mono
import java.util.*

@Service
@Transactional(readOnly = true)
class UserService(private val userRepository: UserRepository) {

    fun checkUsersExists(userId: UUID) {
        findUserById(userId).switchIfEmpty(Mono.error(UserNotFoundException()))
    }

    private fun findUserById(userId: UUID) = userRepository.findById(userId)
}

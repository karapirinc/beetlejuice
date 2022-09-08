package com.karapirinc.beetlejuice.user.repository

import com.karapirinc.beetlejuice.user.model.User
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import java.util.*


@Repository
interface UserRepository : ReactiveCrudRepository<User, UUID>

package com.karapirinc.beetlejuice.user.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.util.*

@Table(name = "users")
class User(
    @Id
    var id: UUID = UUID.randomUUID()
)
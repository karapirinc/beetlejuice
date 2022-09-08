package com.karapirinc.beetlejuice.bug.rest.dto

import com.karapirinc.beetlejuice.bug.model.BugPriority
import com.karapirinc.beetlejuice.bug.model.BugStatus
import java.util.*

data class BugSearchRequestDTO(
    val subject: String?, val description: String?, val assignee: UUID?, val priority: BugPriority?, val status: BugStatus?
)

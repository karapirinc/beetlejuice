package com.karapirinc.beetlejuice.bug.rest.dto

import com.karapirinc.beetlejuice.bug.model.BugPriority
import com.karapirinc.beetlejuice.bug.model.BugStatus
import java.time.Instant
import java.util.*

data class BugSearchResponseDTO(
    val bugId: UUID,
    val subject: String,
    val description: String,
    val assigneeId: UUID?,
    val reporterId: UUID,
    val priority: BugPriority,
    val status: BugStatus,
    val createdAt: Instant,
    val updatedAt: Instant
)

package com.karapirinc.beetlejuice.bug.rest.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.karapirinc.beetlejuice.bug.model.BugPriority
import com.karapirinc.beetlejuice.bug.model.BugStatus
import java.time.Instant
import java.util.*

data class BugSearchResponseDTO(
    @JsonProperty("bug_id")
    val bugId: UUID,
    val subject: String,
    val description: String,
    @JsonProperty("assignee_id")
    val assigneeId: UUID?,
    @JsonProperty("reporter_id")
    val reporterId: UUID,
    val priority: BugPriority,
    val status: BugStatus,
    @JsonProperty("created_at")
    val createdAt: Instant,
    @JsonProperty("updated_at")
    val updatedAt: Instant
)

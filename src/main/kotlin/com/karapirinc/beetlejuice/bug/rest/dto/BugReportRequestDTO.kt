package com.karapirinc.beetlejuice.bug.rest.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.karapirinc.beetlejuice.bug.model.BugPriority
import java.util.*

/**
 * TODO user_id should be retrieved from security context
 */
data class BugReportRequestDTO(
    @JsonProperty("user_id") val userId: UUID,
    @JsonProperty("assignee_id") val assigneeId: UUID? = null,
    val subject: String,
    val description: String,
    val priority: BugPriority? = BugPriority.MEDIUM
)

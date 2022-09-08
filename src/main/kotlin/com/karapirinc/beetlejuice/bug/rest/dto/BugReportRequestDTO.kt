package com.karapirinc.beetlejuice.bug.rest.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.karapirinc.beetlejuice.bug.model.BugPriority
import java.util.*

/**
 * TODO user_id should be retrieved from security context
 */
data class BugReportRequestDTO(
    @JsonProperty(required = true, value = "user_id") val userId: UUID,
    @JsonProperty(value = "assignee_id") val assigneeId: UUID? = null,
    @JsonProperty(required = true) val subject: String,
    @JsonProperty(required = true) val description: String,
    val priority: BugPriority? = BugPriority.MEDIUM
)

package com.karapirinc.beetlejuice.bug.rest.dto

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class BugReportResponseDTO(
    @JsonProperty("bug_id") val bugId: UUID
)

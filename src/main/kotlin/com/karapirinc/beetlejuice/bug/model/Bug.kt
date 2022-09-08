package com.karapirinc.beetlejuice.bug.model

import java.time.Instant
import java.util.*

class Bug(
    var id: UUID,
    var subject: String,
    var description: String,
    var assigneeId: UUID? = null,
    var reporterId: UUID,
    var priority: BugPriority,
    var status: BugStatus,
    var createdAt: Instant,
    var updatedAt: Instant
)

package com.karapirinc.beetlejuice.bug.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import java.time.Instant
import java.util.*

/**
 * TODO Keep bug change history with hibernate envers
 */
class Bug(
    @Id
    var id: UUID = UUID.randomUUID(),
    var subject: String,
    var description: String,
    @Column("assignee_id")
    var assigneeId: UUID? = null,
    @Column("reporter_id")
    var reporterId: UUID,
    var priority: BugPriority,
    var status: BugStatus,
    @Column("created_at")
    var createdAt: Instant = Instant.now(),
    @Column("updated_at")
    var updatedAt: Instant? = null
)

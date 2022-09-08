package com.karapirinc.beetlejuice.bug.service

import com.karapirinc.beetlejuice.bug.model.Bug
import com.karapirinc.beetlejuice.bug.rest.dto.BugReportRequestDTO
import com.karapirinc.beetlejuice.bug.rest.dto.BugSearchRequestDTO
import com.karapirinc.beetlejuice.bug.rest.dto.BugUpdateRequestDTO
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
class BugService {
    fun createBug(request: BugReportRequestDTO): UUID {
        TODO("Not yet implemented")

        return UUID.randomUUID()
    }

    fun updateBug(request: BugUpdateRequestDTO) {
        TODO("Not yet implemented")
    }

    fun deleteBug(bugId: UUID) {
        TODO("Not yet implemented")
    }

    fun fetchBug(bugId: UUID): Bug {
        TODO("Not yet implemented")
    }

    fun searchBugs(request: BugSearchRequestDTO): List<Bug> {
        TODO("Not yet implemented")
    }
}

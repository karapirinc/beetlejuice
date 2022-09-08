package com.karapirinc.beetlejuice.bug.service

import com.karapirinc.beetlejuice.bug.exception.BugNotFoundException
import com.karapirinc.beetlejuice.bug.model.Bug
import com.karapirinc.beetlejuice.bug.model.BugPriority
import com.karapirinc.beetlejuice.bug.model.BugStatus
import com.karapirinc.beetlejuice.bug.repository.BugRepository
import com.karapirinc.beetlejuice.bug.rest.dto.BugReportRequestDTO
import com.karapirinc.beetlejuice.bug.rest.dto.BugSearchRequestDTO
import com.karapirinc.beetlejuice.bug.rest.dto.BugUpdateRequestDTO
import com.karapirinc.beetlejuice.user.service.UserService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Instant
import java.util.*

@Service
@Transactional
class BugService(private val userService: UserService, private val bugRepository: BugRepository) {

    /**
     * Creates the reported bug
     */
    fun createBug(request: BugReportRequestDTO): Mono<UUID> {
        validateReportBugRequest(request)

        return bugRepository.save(with(request) {
            Bug(
                reporterId = userId,
                assigneeId = assigneeId,
                subject = subject,
                description = description,
                status = BugStatus.TO_DO,
                priority = priority ?: BugPriority.MEDIUM
            )
        }).map { it.id }

    }

    private fun validateReportBugRequest(request: BugReportRequestDTO) {
        require(request.subject.isNotEmpty())
        require(request.description.isNotEmpty())
        userService.checkUsersExists(request.userId)
        request.assigneeId?.let { userService.checkUsersExists(request.assigneeId) }
    }

    /**
     * Updates changed fields of a bug
     */
    fun updateBug(request: BugUpdateRequestDTO) {
        validateUpdateBugRequest(request)

        with(request) {
            bugRepository.findById(bugId)
                .switchIfEmpty(Mono.error(BugNotFoundException.forBugId(bugId)))
                .map {
                    it.subject = subject ?: it.subject
                    it.description = description ?: it.description
                    it.assigneeId = assigneeId ?: it.assigneeId
                    it.priority = priority ?: it.priority
                    it.status = status ?: it.status
                    it.updatedAt = Instant.now()
                }
        }
    }

    private fun validateUpdateBugRequest(request: BugUpdateRequestDTO) {
        request.subject?.let { require(it.isNotEmpty()) }
        request.description?.let { require(it.isNotEmpty()) }
        userService.checkUsersExists(request.userId)
        request.assigneeId?.let { userService.checkUsersExists(request.assigneeId) }
    }

    fun deleteBug(bugId: UUID) {
        bugRepository.deleteById(bugId)
    }

    @Transactional(readOnly = true)
    fun fetchBug(bugId: UUID): Mono<Bug> {
        return bugRepository.findById(bugId)
    }

    @Transactional(readOnly = true)
    fun searchBugs(request: BugSearchRequestDTO): Flux<Bug> {
        return bugRepository.findBySearchText(request.searchText)
    }
}

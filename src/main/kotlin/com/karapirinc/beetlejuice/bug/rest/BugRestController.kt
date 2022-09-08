package com.karapirinc.beetlejuice.bug.rest

import com.karapirinc.beetlejuice.bug.rest.dto.*
import com.karapirinc.beetlejuice.bug.service.BugService
import org.springframework.http.HttpStatus.*
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

/**
 * Experiment lambda based approach
 */
@RestController
@RequestMapping(ApiBase.BUGS_PATH)
class BugRestController(private val bugService: BugService) {

    @PostMapping
    @ResponseStatus(CREATED)
    fun reportBug(@RequestBody request: BugReportRequestDTO): ResponseEntity<BugReportResponseDTO> {
        val bugId = bugService.createBug(request)
        return ResponseEntity.status(CREATED).body(BugReportResponseDTO(bugId))
    }

    @PutMapping
    @ResponseStatus(ACCEPTED)
    fun updateBug(@RequestBody request: BugUpdateRequestDTO): ResponseEntity.BodyBuilder {
        bugService.updateBug(request)
        return ResponseEntity.status(ACCEPTED)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    fun deleteBug(@PathVariable("id") bugId: UUID): ResponseEntity.BodyBuilder {
        bugService.deleteBug(bugId = bugId)
        return ResponseEntity.ok()
    }

    @GetMapping("/{id}")
    @ResponseStatus(OK)
    fun fetchBug(@PathVariable("id") bugId: UUID): ResponseEntity<BugSearchResponseDTO> {
        return ResponseEntity.ok()
            .body(
                with(bugService.fetchBug(bugId = bugId)) {
                    BugSearchResponseDTO(
                        bugId = id,
                        subject = subject,
                        description = description,
                        assigneeId = assigneeId,
                        reporterId = reporterId,
                        priority = priority,
                        status = status,
                        createdAt = createdAt,
                        updatedAt = updatedAt
                    )
                })
    }

    /**
     * TODO add pagination and sorting
     */
    @PostMapping("/search")
    @ResponseStatus(OK)
    fun searchBugs(@RequestBody searchCriteria: BugSearchRequestDTO): ResponseEntity<List<BugSearchResponseDTO>> {
        return ResponseEntity.ok()
            .body(bugService.searchBugs(searchCriteria).map {
                BugSearchResponseDTO(
                    bugId = it.id,
                    subject = it.subject,
                    description = it.description,
                    assigneeId = it.assigneeId,
                    reporterId = it.reporterId,
                    priority = it.priority,
                    status = it.status,
                    createdAt = it.createdAt,
                    updatedAt = it.updatedAt
                )
            })
    }
}
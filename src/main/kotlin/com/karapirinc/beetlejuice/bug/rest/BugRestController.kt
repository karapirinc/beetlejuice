package com.karapirinc.beetlejuice.bug.rest

import com.karapirinc.beetlejuice.bug.rest.dto.*
import com.karapirinc.beetlejuice.bug.service.BugService
import com.karapirinc.beetlejuice.config.ApiBase
import org.springframework.http.HttpStatus.*
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*

/**
 * Experiment lambda based approach
 */
@RestController
@RequestMapping(ApiBase.BUGS_PATH)
class BugRestController(private val bugService: BugService) {

    @PostMapping
    @ResponseStatus(CREATED)
    fun reportBug(@Validated @RequestBody request: BugReportRequestDTO): ResponseEntity<Mono<BugReportResponseDTO>> {
        val bugId = bugService.createBug(request)
        return ResponseEntity.status(CREATED).body(bugId.map { BugReportResponseDTO(it) })
    }

    @PutMapping
    @ResponseStatus(ACCEPTED)
    fun updateBug(@Validated @RequestBody request: BugUpdateRequestDTO): ResponseEntity.BodyBuilder {
        bugService.updateBug(request)
        return ResponseEntity.status(ACCEPTED)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    fun deleteBug(@Validated @PathVariable("id") bugId: UUID): ResponseEntity.BodyBuilder {
        bugService.deleteBug(bugId = bugId)
        return ResponseEntity.ok()
    }

    @GetMapping("/{id}")
    @ResponseStatus(OK)
    fun fetchBug(@Validated @PathVariable("id") bugId: UUID): ResponseEntity<Mono<BugSearchResponseDTO>> {
        return ResponseEntity.ok()
            .body(
                bugService.fetchBug(bugId = bugId).map {
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

    /**
     * TODO add pagination and sorting
     */
    @PostMapping("/search")
    @ResponseStatus(OK)
    fun searchBugs(@Validated @RequestBody searchCriteria: BugSearchRequestDTO): ResponseEntity<Flux<BugSearchResponseDTO>> {
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
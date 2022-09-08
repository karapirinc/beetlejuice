package com.karapirinc.beetlejuice.bug.rest

import com.karapirinc.beetlejuice.bug.model.Bug
import com.karapirinc.beetlejuice.bug.model.BugPriority
import com.karapirinc.beetlejuice.bug.model.BugStatus
import com.karapirinc.beetlejuice.bug.rest.dto.BugReportRequestDTO
import com.karapirinc.beetlejuice.bug.rest.dto.BugSearchRequestDTO
import com.karapirinc.beetlejuice.bug.rest.dto.BugUpdateRequestDTO
import com.karapirinc.beetlejuice.bug.service.BugService
import com.karapirinc.beetlejuice.config.ApiBase
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.justRun
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.body
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Instant
import java.util.*


@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BugRestControllerIntegrationTest(@Autowired private val webClient: WebTestClient) {

    @MockkBean
    private lateinit var bugService: BugService

    @Test
    fun `should report a bug`() {
        val request = BugReportRequestDTO(
            userId = UUID.randomUUID(),
            subject = "a bug",
            description = "an ordinary bug",
            assigneeId = UUID.randomUUID(),
            priority = BugPriority.MEDIUM
        )

        val bugId = UUID.randomUUID()

        every { bugService.createBug(request) } returns Mono.just(bugId)

        webClient.post().uri(ApiBase.BUGS_PATH).contentType(APPLICATION_JSON).body(Mono.just(request))
            .exchange().expectStatus().isCreated
            .expectBody()
            .jsonPath("$.bug_id").isEqualTo(bugId.toString())

        verify { bugService.createBug(request) }

    }

    @Test
    fun `should update a bug`() {

        val request = BugUpdateRequestDTO(
            bugId = UUID.randomUUID(),
            userId = UUID.randomUUID(),
            assigneeId = UUID.randomUUID(),
            subject = "update subject",
            description = "update description",
            priority = BugPriority.HIGH
        )
        justRun { bugService.updateBug(request) }

        webClient.put().uri(ApiBase.BUGS_PATH).contentType(APPLICATION_JSON).body(Mono.just(request))
            .exchange()
            .expectStatus().isAccepted

        verify { bugService.updateBug(request) }

    }

    @Test
    fun `should delete a bug`() {
        val bugId = UUID.randomUUID()

        justRun { bugService.deleteBug(bugId) }

        webClient.delete().uri("${ApiBase.BUGS_PATH}/{id}", mapOf("id" to bugId))
            .exchange()
            .expectStatus().isNoContent

        verify { bugService.deleteBug(bugId) }

    }

    @Test
    fun `should fetch a bug`() {
        val bugId = UUID.randomUUID()
        val assigneeId = UUID.randomUUID()

        val bug = Bug(
            id = bugId,
            subject = "subject",
            description = "description",
            assigneeId = assigneeId,
            reporterId = UUID.randomUUID(),
            priority = BugPriority.HIGH,
            status = BugStatus.TO_DO,
            createdAt = Instant.now(),
            updatedAt = Instant.now()
        )

        every { bugService.fetchBug(bugId) } returns Mono.just(bug)

        webClient.get().uri("${ApiBase.BUGS_PATH}/{id}", mapOf("id" to bugId))
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.bug_id").isEqualTo(bugId.toString())
            .jsonPath("$.subject").isEqualTo(bug.subject)
            .jsonPath("$.description").isEqualTo(bug.description)
            .jsonPath("$.status").isEqualTo(bug.status.name)
            .jsonPath("$.reporter_id").isEqualTo(bug.reporterId.toString())
            .jsonPath("$.assignee_id").isEqualTo(assigneeId.toString())
            .jsonPath("$.priority").isEqualTo(bug.priority.name)
            .jsonPath("$.created_at").isEqualTo(bug.createdAt.toString())
            .jsonPath("$.updated_at").isEqualTo(bug.updatedAt.toString())

    }

    @Test
    fun `should search a bug`() {
        val request = BugSearchRequestDTO(
            searchText = "a bug"
        )

        val bugId1 = UUID.randomUUID()
        val bugId2 = UUID.randomUUID()
        val subject1 = "A"
        val subject2 = "B"

        val bug1 = Bug(
            id = bugId1,
            subject = subject1,
            description = "description",
            assigneeId = UUID.randomUUID(),
            reporterId = UUID.randomUUID(),
            priority = BugPriority.HIGH,
            status = BugStatus.TO_DO,
            createdAt = Instant.now(),
            updatedAt = Instant.now()
        )

        val bug2 = Bug(
            id = bugId2,
            subject = subject2,
            description = "description",
            assigneeId = UUID.randomUUID(),
            reporterId = UUID.randomUUID(),
            priority = BugPriority.HIGH,
            status = BugStatus.TO_DO,
            createdAt = Instant.now(),
            updatedAt = Instant.now()
        )
        every { bugService.searchBugs(request) } returns Flux.just(bug1, bug2)

        webClient.post().uri("${ApiBase.BUGS_PATH}/search").contentType(APPLICATION_JSON)
            .body(Mono.just(request)).exchange()
            .expectStatus().isOk
            .expectHeader().contentType(APPLICATION_JSON)
            .expectBody()
            .jsonPath("@.[0].bug_id").isEqualTo(bugId1.toString())
            .jsonPath("@.[0].subject").isEqualTo(subject1)
            .jsonPath("@.[1].bug_id").isEqualTo(bugId2.toString())
            .jsonPath("@.[1].subject").isEqualTo(subject2)
    }
}


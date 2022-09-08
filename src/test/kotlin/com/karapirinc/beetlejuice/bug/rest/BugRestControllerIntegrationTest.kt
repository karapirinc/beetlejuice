package com.karapirinc.beetlejuice.bug.rest

import com.karapirinc.beetlejuice.bug.model.BugPriority
import com.karapirinc.beetlejuice.bug.model.BugStatus
import com.karapirinc.beetlejuice.bug.rest.dto.BugReportRequestDTO
import com.karapirinc.beetlejuice.bug.rest.dto.BugSearchRequestDTO
import com.karapirinc.beetlejuice.bug.rest.dto.BugUpdateRequestDTO
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.body
import reactor.core.publisher.Mono
import java.time.Instant
import java.util.*


@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BugRestControllerIntegrationTest(@Autowired private val webClient: WebTestClient) {

    @Test
    fun `should report a bug`() {
        val request = BugReportRequestDTO(
                userId = UUID.randomUUID(),
                subject = "a bug",
                description = "an ordinary bug",
                assigneeId = UUID.randomUUID(),
                priority = BugPriority.MEDIUM)

        val bugId = UUID.randomUUID()
        webClient.post().uri("/bug").contentType(MediaType.APPLICATION_JSON).body(Mono.just(request))
                .exchange().expectStatus().isCreated
                .expectBody()
                .jsonPath("$.bug_id").isEqualTo(bugId)
    }

    @Test
    fun `should update a bug`() {
        val request = BugUpdateRequestDTO(
                bugId = UUID.randomUUID(),
                userId = UUID.randomUUID(),
                assigneeId = UUID.randomUUID(),
                subject = "update subject",
                description = "update description",
                priority = BugPriority.HIGH)

        val bugId = UUID.randomUUID()
        webClient.put().uri("/bug").contentType(MediaType.APPLICATION_JSON).body(Mono.just(request)).exchange()
                .expectStatus().isOk
                .expectBody()
                .jsonPath("$.bug_id").isEqualTo(bugId)
    }

    @Test
    fun `should delete a bug`() {
        webClient.delete().uri("/bug").attribute("id", UUID.randomUUID()).exchange().expectStatus().isNoContent
    }

    @Test
    fun `should fetch a bug`() {
        val bugId = UUID.randomUUID()
        val assigneeId = UUID.randomUUID()
        val reporterId = UUID.randomUUID()
        val subject = "subject"
        val description = "description"
        val priority = BugPriority.HIGH
        val status = BugStatus.TODO
        val createdAt = Instant.now()
        val updatedAt = Instant.now()

        webClient.get().uri("/bug").attribute("id", bugId).exchange()
                .expectStatus().isOk
                .expectBody()
                .jsonPath("$.bug_id").isEqualTo(bugId)
                .jsonPath("$.subject").isEqualTo(subject)
                .jsonPath("$.description").isEqualTo(description)
                .jsonPath("$.status").isEqualTo(status)
                .jsonPath("$.reporter_id").isEqualTo(reporterId)
                .jsonPath("$.assignee_id").isEqualTo(assigneeId)
                .jsonPath("$.priority").isEqualTo(priority)
                .jsonPath("$.created_at").isEqualTo(createdAt)
                .jsonPath("$.updated_at").isEqualTo(updatedAt)
    }

    @Test
    fun `should search a bug`() {
        val request = BugSearchRequestDTO(subject = "a bug", description = "an ordinary bug", assignee = UUID.randomUUID(), priority = BugPriority.MEDIUM, status = BugStatus.TODO)

        val bugId1 = UUID.randomUUID()
        val bugId2 = UUID.randomUUID()
        val subject1 = "A"
        val subject2 = "B"
        webClient.post().uri("/bug/search").contentType(MediaType.APPLICATION_JSON).body(Mono.just(request)).exchange()
                .expectStatus().isOk
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("@.[0].bug_id").isEqualTo(bugId1)
                .jsonPath("@.[0].subject").isEqualTo(subject1)
                .jsonPath("@.[1].bug_id").isEqualTo(bugId2)
                .jsonPath("@.[1].subject").isEqualTo(subject2)
    }
}


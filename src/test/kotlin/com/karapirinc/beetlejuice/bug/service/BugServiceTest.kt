package com.karapirinc.beetlejuice.bug.service

import com.karapirinc.beetlejuice.bug.model.BugPriority
import com.karapirinc.beetlejuice.bug.repository.BugRepository
import com.karapirinc.beetlejuice.bug.rest.dto.BugReportRequestDTO
import com.karapirinc.beetlejuice.bug.rest.dto.BugSearchRequestDTO
import com.karapirinc.beetlejuice.bug.rest.dto.BugUpdateRequestDTO
import com.karapirinc.beetlejuice.user.service.UserService
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import java.util.*

@ExtendWith(MockKExtension::class)
internal class BugServiceTest(
    @RelaxedMockK
    val bugRepository: BugRepository,
    @RelaxedMockK
    val userService: UserService
) {

    private val bugService = BugService(userService, bugRepository)

    @Nested
    inner class WhenReportBug {

        private val request = BugReportRequestDTO(
            userId = UUID.randomUUID(),
            subject = "",
            description = "",
        )

        @Test
        fun `fail if mandatory fields are missing`() {
            assertThrows<IllegalArgumentException> {
                bugService.createBug(request)
            }
        }

        @Test
        fun `fail if users does not exist`() {
            assertThrows<IllegalStateException> {
                bugService.createBug(request)
            }
        }

        @Test
        fun `should create a bug`() {
            val id = bugService.createBug(request)
            assertThat(id).isNotNull
        }
    }

    @Nested
    inner class WhenUpdateBug {

        private val request = BugUpdateRequestDTO(
            bugId = UUID.randomUUID(),
            userId = UUID.randomUUID(),
            assigneeId = UUID.randomUUID(),
            subject = "update subject",
            description = "update description",
            priority = BugPriority.HIGH
        )

        @Test
        fun `fail if bug does not exist`() {
            assertThrows<IllegalStateException> {
                bugService.updateBug(request)
            }
        }

        @Test
        fun `fail if mandatory fields are missing`() {
            assertThrows<IllegalArgumentException> {
                bugService.updateBug(request)
            }
        }

        @Test
        fun `fail if users does not exist`() {
            assertThrows<IllegalStateException> {
                bugService.updateBug(request)
            }
        }

        @Test
        fun `should update a bug`() {
            val id = bugService.updateBug(request)
            assertThat(id).isNotNull
        }
    }

    @Nested
    inner class WhenSearchBug {
        private val request = BugSearchRequestDTO(
            searchText = "a bug",
        )

        @Test
        fun `should return list of bugs`() {
            val result = bugService.searchBugs(request)
            assertThat(result).isNotNull
        }
    }


}
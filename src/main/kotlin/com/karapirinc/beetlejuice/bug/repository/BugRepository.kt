package com.karapirinc.beetlejuice.bug.repository

import com.karapirinc.beetlejuice.bug.model.Bug
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import java.util.*

@Repository
interface BugRepository : ReactiveCrudRepository<Bug, UUID> {

    @Query("SELECT * FROM Bug WHERE subject LIKE '%' + :searchText + '%' OR description LIKE '%' + :searchText + '%' ")
    fun findBySearchText(searchText: String): Flux<Bug>
}

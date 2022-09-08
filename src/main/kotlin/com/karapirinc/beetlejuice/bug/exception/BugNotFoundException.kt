package com.karapirinc.beetlejuice.bug.exception

import java.util.*

class BugNotFoundException(message: String) : RuntimeException(message) {

    companion object {
        @JvmStatic
        fun forBugId(bugId: UUID): BugNotFoundException {
            return BugNotFoundException("bugId=$bugId")
        }
    }

}

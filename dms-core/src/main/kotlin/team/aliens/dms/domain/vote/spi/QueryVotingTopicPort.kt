package team.aliens.dms.domain.vote.spi

import java.time.LocalDateTime
import java.util.UUID

interface QueryVotingTopicPort {
    fun findStartTimeById(id:UUID):LocalDateTime?
    fun findEndTimeById(id:UUID):LocalDateTime?
}
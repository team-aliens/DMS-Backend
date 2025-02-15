package team.aliens.dms.domain.vote.spi

import team.aliens.dms.domain.vote.model.VotingTopic
import java.time.LocalDateTime
import java.util.UUID

interface QueryVotingTopicPort {

    fun queryStartTimeById(id: UUID): LocalDateTime?

    fun queryEndTimeById(id: UUID): LocalDateTime?

    fun queryVotingTopicById(id: UUID): VotingTopic?

    fun queryAllVotingTopic(): List<VotingTopic?>
}

package team.aliens.dms.domain.vote.stub

import team.aliens.dms.domain.vote.model.VoteType
import team.aliens.dms.domain.vote.model.VotingTopic
import java.time.LocalDateTime
import java.util.UUID

internal fun createVotingTopicStub(
    id: UUID = UUID.randomUUID(),
    topicName: String = "Test Voting Topic",
    description: String? = "Test description",
    startTime: LocalDateTime = LocalDateTime.now().minusDays(1),
    endTime: LocalDateTime = LocalDateTime.now().plusDays(1),
    voteType: VoteType = VoteType.OPTION_VOTE,
    managerId: UUID = UUID.randomUUID()
) = VotingTopic(
    id = id,
    topicName = topicName,
    description = description,
    startTime = startTime,
    endTime = endTime,
    voteType = voteType,
    managerId = managerId
)

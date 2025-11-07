package team.aliens.dms.domain.vote.model

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import team.aliens.dms.domain.vote.exception.InvalidVotingPeriodException
import team.aliens.dms.domain.vote.stub.createVotingTopicStub
import java.time.LocalDateTime
import java.util.UUID

class VotingTopicTest : DescribeSpec({

    describe("checkVotingPeriod") {
        context("현재 시간이 투표 기간 내에 있으면") {
            val votingTopic = createVotingTopicStub(
                startTime = LocalDateTime.now().minusHours(1),
                endTime = LocalDateTime.now().plusHours(1)
            )
            val voteTopicId = UUID.randomUUID()

            it("예외가 발생하지 않는다") {
                shouldNotThrowAny {
                    votingTopic.checkVotingPeriod(voteTopicId)
                }
            }
        }

        context("현재 시간이 투표 시작 전이면") {
            val votingTopic = createVotingTopicStub(
                startTime = LocalDateTime.now().plusHours(1),
                endTime = LocalDateTime.now().plusHours(2)
            )
            val voteTopicId = UUID.randomUUID()

            it("InvalidVotingPeriodException을 발생시킨다") {
                shouldThrow<InvalidVotingPeriodException> {
                    votingTopic.checkVotingPeriod(voteTopicId)
                }
            }
        }

        context("현재 시간이 투표 종료 후이면") {
            val votingTopic = createVotingTopicStub(
                startTime = LocalDateTime.now().minusHours(2),
                endTime = LocalDateTime.now().minusHours(1)
            )
            val voteTopicId = UUID.randomUUID()

            it("InvalidVotingPeriodException을 발생시킨다") {
                shouldThrow<InvalidVotingPeriodException> {
                    votingTopic.checkVotingPeriod(voteTopicId)
                }
            }
        }

        context("현재 시간이 시작 시간과 정확히 같으면") {
            val now = LocalDateTime.now()
            val votingTopic = createVotingTopicStub(
                startTime = now,
                endTime = now.plusHours(1)
            )
            val voteTopicId = UUID.randomUUID()

            it("InvalidVotingPeriodException을 발생시킨다") {
                shouldThrow<InvalidVotingPeriodException> {
                    votingTopic.checkVotingPeriod(voteTopicId)
                }
            }
        }

        context("현재 시간이 종료 시간과 정확히 같으면") {
            val now = LocalDateTime.now()
            val votingTopic = createVotingTopicStub(
                startTime = now.minusHours(1),
                endTime = now
            )
            val voteTopicId = UUID.randomUUID()

            it("InvalidVotingPeriodException을 발생시킨다") {
                shouldThrow<InvalidVotingPeriodException> {
                    votingTopic.checkVotingPeriod(voteTopicId)
                }
            }
        }
    }
})

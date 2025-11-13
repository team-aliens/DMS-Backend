package team.aliens.dms.domain.vote.model

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import team.aliens.dms.domain.vote.exception.InvalidPeriodException
import team.aliens.dms.domain.vote.stub.createVotingTopicStub
import java.time.LocalDateTime

class VotingTopicTest : DescribeSpec({

    describe("InvalidPeriodException") {
        context("시작시간이 투표 종료 시간보다 앞에있고, 투표종료 시간이 현재시각보다 뒤에 있으면") {
            val votingTopic = createVotingTopicStub(
                startTime = LocalDateTime.now().minusHours(1),
                endTime = LocalDateTime.now().plusHours(1)
            )

            it("예외가 발생하지 않는다") {
                shouldNotThrowAny {
                    votingTopic.checkVotingTopicPeriod()
                }
            }
        }

        context("시작시간이 투표 종료 시간보다 앞에있고, 투표종료 시간이 현재시각보다 앞에 있으면") {
            val votingTopic = createVotingTopicStub(
                startTime = LocalDateTime.now().minusHours(2),
                endTime = LocalDateTime.now().minusHours(1)
            )

            it("InvalidPeriodException을 발생시킨다") {
                shouldThrow<InvalidPeriodException> {
                    votingTopic.checkVotingTopicPeriod()
                }
            }
        }

        context("시작시간이 투표 종료 시간보다 뒤에있고, 투표종료 시간이 현재시각보다 뒤에 있으면") {
            val votingTopic = createVotingTopicStub(
                startTime = LocalDateTime.now().plusHours(2),
                endTime = LocalDateTime.now().plusHours(1)
            )

            it("InvalidPeriodException을 발생시킨다") {
                shouldThrow<InvalidPeriodException> {
                    votingTopic.checkVotingTopicPeriod()
                }
            }
        }
    }
})

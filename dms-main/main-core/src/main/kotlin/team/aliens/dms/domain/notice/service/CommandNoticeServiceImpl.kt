package team.aliens.dms.domain.notice.service

import team.aliens.dms.common.annotation.Service
import team.aliens.dms.common.spi.NotificationEventPort
import team.aliens.dms.common.spi.SecurityPort
import team.aliens.dms.common.spi.TaskSchedulerPort
import team.aliens.dms.contract.model.notification.NotificationInfo
import team.aliens.dms.contract.model.notification.Topic
import team.aliens.dms.domain.notice.model.Notice
import team.aliens.dms.domain.notice.spi.CommandNoticePort
import team.aliens.dms.domain.student.exception.StudentNotFoundException
import team.aliens.dms.domain.student.spi.QueryStudentPort
import team.aliens.dms.domain.user.spi.QueryUserPort
import team.aliens.dms.domain.vote.exception.VotingTopicNotFoundException
import team.aliens.dms.domain.vote.model.VoteType
import team.aliens.dms.domain.vote.model.VotingTopic
import team.aliens.dms.domain.vote.spi.QueryVotePort
import team.aliens.dms.domain.vote.spi.QueryVotingTopicPort
import java.time.LocalDateTime
import java.util.UUID

@Service
class CommandNoticeServiceImpl(
    private val commandNoticePort: CommandNoticePort,
    private val notificationEventPort: NotificationEventPort,
    private val securityPort: SecurityPort,
    private val taskSchedulerPort: TaskSchedulerPort,
    private val queryVotingTopicPort: QueryVotingTopicPort,
    private val queryVotePort: QueryVotePort,
    private val queryStudentPort: QueryStudentPort,
    private val queryUserPort: QueryUserPort,
) : CommandNoticeService {

    override fun saveNotice(notice: Notice): Notice {
        val schoolId = securityPort.getCurrentUserSchoolId()

        val userIds = queryUserPort.queryUsersBySchoolId(schoolId)
            .map { it.id }

        return commandNoticePort.saveNotice(notice)
            .also {
                notificationEventPort.publishNotificationToApplicant(
                    userIds,
                    NotificationInfo(
                        schoolId = schoolId,
                        topic = Topic.NOTICE,
                        linkIdentifier = notice.id.toString(),
                        title = notice.title,
                        content = "기숙사 공지가 등록되었습니다.",
                        threadId = notice.id.toString(),
                        isSaveRequired = true
                    )
                )
            }
    }

    override fun deleteNotice(notice: Notice) {
        commandNoticePort.deleteNotice(notice)
    }

    override fun voteResultNotice(votingTopicId: UUID, startTime: LocalDateTime, isReNotice: Boolean, managerId: UUID, schoolId: UUID) {
        val userIds = queryUserPort.queryUsersBySchoolId(schoolId)
            .map { it.id }

        val reNoticePrefix = if (isReNotice) "[재공지]" else ""
        val votingTopic = queryVotingTopicPort.queryVotingTopicById(votingTopicId) ?: throw VotingTopicNotFoundException
        val content = generateVoteResultContent(votingTopic)

        commandNoticePort.saveNotice(
            Notice(
                title = reNoticePrefix + votingTopic.topicName,
                content = content,
                managerId = managerId,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now()
            )
        ).also {
            notificationEventPort.publishNotificationToApplicant(
                userIds,
                NotificationInfo(
                    schoolId = schoolId,
                    topic = Topic.NOTICE,
                    linkIdentifier = it.id.toString(),
                    title = it.title,
                    content = "기숙사 공지가 등록되었습니다.",
                    threadId = it.id.toString(),
                    isSaveRequired = true
                )
            )
        }
    }

    private fun generateVoteResultContent(votingTopic: VotingTopic): String {
        return when (votingTopic.voteType) {
            VoteType.OPTION_VOTE, VoteType.APPROVAL_VOTE -> generateOptionVoteResult(votingTopic.id)
            VoteType.STUDENT_VOTE, VoteType.MODEL_STUDENT_VOTE -> generateStudentVoteResult(votingTopic)
        }
    }

    private fun generateOptionVoteResult(votingTopicId: UUID): String {
        val result = queryVotePort.queryOptionVotingByVotingTopicId(votingTopicId)
        val stringBuilder = StringBuilder()
        var currentRank = 0
        var displayRank = 0
        var previousVotes: Int? = null

        result.forEach {
            if (previousVotes == null || previousVotes != it.votes) {
                currentRank = displayRank + 1
            }
            displayRank++
            stringBuilder.append("${currentRank}등 : ${it.name} (${it.votes}표)\n")
            previousVotes = it.votes
        }
        return stringBuilder.toString()
    }

    private fun generateStudentVoteResult(votingTopic: VotingTopic): String {
        val stringBuilder = StringBuilder()

        listOf(1, 2, 3).forEach { grade ->
            val result =
                if (votingTopic.voteType == VoteType.MODEL_STUDENT_VOTE)
                    queryVotePort.queryModelStudentVotingByVotingTopicIdAndGrade(votingTopic.id, grade).take(3)
                else
                    queryVotePort.queryStudentVotingByVotingTopicIdAndGrade(votingTopic.id, grade).take(1)

            stringBuilder.append("${grade}학년 : ")
            val line = result.joinToString(", ") { votingResult ->
                val student = queryStudentPort.queryStudentById(votingResult.id)
                    ?: throw StudentNotFoundException
                "${student.gcn} ${student.name} (${votingResult.votes}표)"
            }
            stringBuilder.append("$line \n")
        }

        return stringBuilder.toString()
    }
}

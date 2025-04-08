package team.aliens.dms.domain.notice.service

import team.aliens.dms.common.annotation.Service
import team.aliens.dms.common.spi.NotificationEventPort
import team.aliens.dms.common.spi.SecurityPort
import team.aliens.dms.common.spi.TaskSchedulerPort
import team.aliens.dms.domain.notice.model.Notice
import team.aliens.dms.domain.notice.spi.CommandNoticePort
import team.aliens.dms.domain.notification.model.DeviceToken
import team.aliens.dms.domain.notification.model.Notification
import team.aliens.dms.domain.notification.spi.QueryDeviceTokenPort
import team.aliens.dms.domain.student.exception.StudentNotFoundException
import team.aliens.dms.domain.student.spi.QueryStudentPort
import team.aliens.dms.domain.vote.exception.VotingTopicNotFoundException
import team.aliens.dms.domain.vote.model.VoteType
import team.aliens.dms.domain.vote.spi.QueryVotePort
import team.aliens.dms.domain.vote.spi.QueryVotingTopicPort
import java.time.LocalDateTime
import java.util.UUID

@Service
class CommandNoticeServiceImpl(
    private val commandNoticePort: CommandNoticePort,
    private val notificationEventPort: NotificationEventPort,
    private val securityPort: SecurityPort,
    private val deviceTokenPort: QueryDeviceTokenPort,
    private val taskSchedulerPort: TaskSchedulerPort,
    private val queryVotingTopicPort: QueryVotingTopicPort,
    private val queryVotePort: QueryVotePort,
    private val queryStudentPort: QueryStudentPort
) : CommandNoticeService {

    override fun saveNotice(notice: Notice): Notice {
        val schoolId = securityPort.getCurrentUserSchoolId()
        val deviceTokens: List<DeviceToken> = deviceTokenPort.queryDeviceTokensBySchoolId(schoolId)

        return commandNoticePort.saveNotice(notice)
            .also {
                notificationEventPort.publishNotificationToApplicant(
                    deviceTokens, Notification.NoticeNotification(schoolId, it)
                )
            }
    }

    override fun deleteNotice(notice: Notice) {
        commandNoticePort.deleteNotice(notice)
    }

    override fun scheduleVoteResultNotice(votingTopicId: UUID, reservedTime: LocalDateTime, isReNotice: Boolean) {
        val managerId = securityPort.getCurrentUserId()
        val schoolId = securityPort.getCurrentUserSchoolId()
        val deviceTokens = deviceTokenPort.queryDeviceTokensBySchoolId(schoolId)
        val reNoticePrefix = if (isReNotice) "[재공지]" else ""

        val votingTopic = queryVotingTopicPort.queryVotingTopicById(votingTopicId) ?: throw VotingTopicNotFoundException
        var content = ""

        when (votingTopic.voteType) {
            VoteType.OPTION_VOTE, VoteType.APPROVAL_VOTE -> {
                val result =
                    queryVotePort.queryOptionVotingByVotingTopicId(votingTopicId)
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
                content = stringBuilder.toString()
            }

            VoteType.STUDENT_VOTE, VoteType.MODEL_STUDENT_VOTE -> {
                val stringBuilder = StringBuilder()
                listOf(1, 2, 3).forEach { grade ->
                    val result =
                        if (votingTopic.voteType == VoteType.MODEL_STUDENT_VOTE)
                            queryVotePort.queryModelStudentVotingByVotingTopicIdAndGrade(votingTopicId, grade).take(3)
                        else
                            queryVotePort.queryStudentVotingByVotingTopicIdAndGrade(votingTopicId, grade).take(1)

                    stringBuilder.append("${grade}학년 : ")
                    var line = result.joinToString(", ") { votingResult ->
                        val student =
                            queryStudentPort.queryStudentById(votingResult.id) ?: throw StudentNotFoundException
                        "${student.gcn} ${student.name} (${votingResult.votes}표)"
                    }
                    stringBuilder.append("$line \n")
                }
                content = stringBuilder.toString()
            }
        }

        taskSchedulerPort.scheduleTask(
            votingTopicId, {
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
                        deviceTokens, Notification.NoticeNotification(schoolId, it)
                    )
                }
            }, reservedTime
        )
    }
}

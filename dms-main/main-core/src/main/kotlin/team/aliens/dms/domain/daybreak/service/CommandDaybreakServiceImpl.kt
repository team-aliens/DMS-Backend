package team.aliens.dms.domain.daybreak.service

import team.aliens.dms.common.annotation.Service
import team.aliens.dms.common.spi.NotificationEventPort
import team.aliens.dms.contract.model.notification.NotificationInfo
import team.aliens.dms.contract.model.notification.Topic
import team.aliens.dms.domain.daybreak.model.DaybreakStudyApplication
import team.aliens.dms.domain.daybreak.model.DaybreakStudyType
import team.aliens.dms.domain.daybreak.model.Status
import team.aliens.dms.domain.daybreak.spi.CommandDaybreakStudyApplicationPort
import team.aliens.dms.domain.daybreak.spi.CommandDaybreakStudyTypePort
import team.aliens.dms.domain.student.service.StudentService

@Service
class CommandDaybreakServiceImpl(
    private val commandDaybreakStudyApplicationPort: CommandDaybreakStudyApplicationPort,
    private val commandDaybreakStudyTypePort: CommandDaybreakStudyTypePort,
    private val notificationEventPort: NotificationEventPort,
    private val studentService: StudentService
) : CommandDaybreakService {

    override fun saveDaybreakStudyApplication(application: DaybreakStudyApplication) {
        commandDaybreakStudyApplicationPort.saveDaybreakStudyApplication(application)
    }

    override fun saveDaybreakStudyType(type: DaybreakStudyType) {
        commandDaybreakStudyTypePort.saveDaybreakStudyType(type)
    }

    override fun saveAllDaybreakStudyApplications(applications: List<DaybreakStudyApplication>) {

        commandDaybreakStudyApplicationPort.saveAllDaybreakStudyApplications(applications)

        val firstApplication = applications.first()
        val studentIds = applications.map { it.studentId}
        val userIds = studentService.getAllStudentsByIdsIn(studentIds).map { it.userId!! }

        if(firstApplication.status == Status.SECOND_APPROVED || firstApplication.status == Status.REJECTED) {

            val notificationInfo = NotificationInfo(
                schoolId = firstApplication.schoolId,
                topic = Topic.DAYBREAK_STUDY_APPLICATION,
                linkIdentifier = firstApplication.id.toString(),
                title = firstApplication.getTitle(),
                content = "새벽 자습 신청 상태가 변경되었습니다.",
                threadId = firstApplication.id.toString(),
                isSaveRequired = true
            )
            notificationEventPort.publishNotificationToApplicant(userIds, notificationInfo)
        }
    }
}

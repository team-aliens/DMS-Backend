package team.aliens.dms.domain.bug.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.bug.dto.CreateBugReportRequest
import team.aliens.dms.domain.bug.model.BugAttachment
import team.aliens.dms.domain.bug.model.BugReport
import team.aliens.dms.domain.bug.model.DevelopmentArea
import team.aliens.dms.domain.bug.service.BugService
import team.aliens.dms.domain.student.service.StudentService
import java.time.LocalDateTime

@UseCase
class CreateBugReportUseCase(
    private val bugService: BugService,
    private val studentService: StudentService
) {

    fun execute(request: CreateBugReportRequest) {
        val student = studentService.getCurrentStudent()

        val attachmentUrls = request.attachmentUrls ?: emptyList()

        bugService.saveBugReport(
            BugReport(
                studentId = student.id,
                content = request.content,
                developmentArea = DevelopmentArea.valueOf(request.developmentArea),
                createdAt = LocalDateTime.now(),
                attachmentUrls = BugAttachment(attachmentUrls)
            )
        )
    }
}

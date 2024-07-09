package team.aliens.dms.domain.outing.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.common.service.security.SecurityService
import team.aliens.dms.domain.outing.dto.request.ApplyOutingRequest
import team.aliens.dms.domain.outing.dto.response.ApplyOutingResponse
import team.aliens.dms.domain.outing.model.OutingApplication
import team.aliens.dms.domain.outing.model.OutingStatus
import team.aliens.dms.domain.outing.service.OutingService
import team.aliens.dms.domain.student.service.StudentService
import java.time.LocalDateTime

@UseCase
class ApplyOutingUseCase(
    private val outingService: OutingService,
    private val studentService: StudentService,
    private val securityService: SecurityService
) {

    fun execute(request: ApplyOutingRequest): ApplyOutingResponse {
        val student = studentService.getCurrentStudent()

        val outingDate = outingService.checkOutingDateBasedOnCurrentTime()

        outingService.checkOutingApplicationAvailable(
            studentId = student.id,
            outingDate = outingDate,
            outingTime = request.outingTime,
            arrivalTime = request.arrivalTime
        )

        val outing = outingService.saveOutingApplication(
            OutingApplication(
                studentId = student.id,
                createdAt = LocalDateTime.now(),
                outingDate = outingDate,
                outingTime = request.outingTime,
                arrivalTime = request.arrivalTime,
                status = OutingStatus.APPROVED,
                reason = request.reason,
                outingTypeTitle = request.outingTypeTitle,
                schoolId = securityService.getCurrentSchoolId(),
                companionIds = request.companionIds
            )
        )

        return ApplyOutingResponse(outing.id)
    }
}


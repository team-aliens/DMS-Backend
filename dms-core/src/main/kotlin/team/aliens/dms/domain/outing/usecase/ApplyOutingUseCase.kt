package team.aliens.dms.domain.outing.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.common.service.security.SecurityService
import team.aliens.dms.domain.outing.dto.ApplyOutingRequest
import team.aliens.dms.domain.outing.dto.ApplyOutingResponse
import team.aliens.dms.domain.outing.model.OutingApplication
import team.aliens.dms.domain.outing.model.OutingStatus
import team.aliens.dms.domain.outing.service.OutingService
import team.aliens.dms.domain.student.service.StudentService
import java.time.LocalDateTime

@UseCase
class ApplyOutingUseCase(
    private val outingService: OutingService,
    private val studentService: StudentService,
    private val securityService: SecurityService,
) {

    fun execute(request: ApplyOutingRequest): ApplyOutingResponse {
        val student = studentService.getCurrentStudent()

        outingService.checkOutingApplicationAvailable(
            studentId = student.id,
            outAt = request.outAt,
            outingTime = request.outingTime,
            arrivalTime = request.arrivalTime
        )

        val outing = outingService.saveOutingApplication(
            OutingApplication(
                studentId = student.id,
                createdAt = LocalDateTime.now(),
                outAt = request.outAt,
                outingTime = request.outingTime,
                arrivalTime = request.arrivalTime,
                status = OutingStatus.REQUESTED,
                reason = request.reason,
                destination = request.destination,
                outingTypeTitle = request.outingTypeTitle,
                schoolId = securityService.getCurrentSchoolId(),
                companionIds = request.companionIds
            )
        )

        return ApplyOutingResponse(outing.id)
    }
}

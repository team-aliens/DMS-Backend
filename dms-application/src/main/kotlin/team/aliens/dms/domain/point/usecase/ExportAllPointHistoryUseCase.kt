package team.aliens.dms.domain.point.usecase

import java.time.LocalDateTime
import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.file.model.File
import team.aliens.dms.domain.file.spi.WriteFilePort
import team.aliens.dms.domain.point.dto.ExportAllPointHistoryResponse
import team.aliens.dms.domain.point.model.PointHistory
import team.aliens.dms.domain.point.spi.PointQuerySchoolPort
import team.aliens.dms.domain.point.spi.PointQueryUserPort
import team.aliens.dms.domain.point.spi.PointSecurityPort
import team.aliens.dms.domain.point.spi.QueryPointHistoryPort
import team.aliens.dms.domain.school.exception.SchoolNotFoundException
import team.aliens.dms.domain.school.model.School
import team.aliens.dms.domain.user.exception.UserNotFoundException

@ReadOnlyUseCase
class ExportAllPointHistoryUseCase(
    private val securityPort: PointSecurityPort,
    private val queryUserPort: PointQueryUserPort,
    private val queryPointHistoryPort: QueryPointHistoryPort,
    private val querySchoolPort: PointQuerySchoolPort,
    private val writeFilePort: WriteFilePort
) {

    fun execute(start: LocalDateTime?, end: LocalDateTime?): ExportAllPointHistoryResponse {

        val currentUserId = securityPort.getCurrentUserId()
        val manager = queryUserPort.queryUserById(currentUserId) ?: throw UserNotFoundException
        val school = querySchoolPort.querySchoolById(manager.schoolId) ?: throw SchoolNotFoundException

        val endOrNow = end ?: LocalDateTime.now()

        val pointHistories = queryPointHistoryPort.queryPointHistoryBySchoolIdAndCreatedAtBetween(
            schoolId = manager.schoolId,
            startAt = start,
            endAt = endOrNow
        )

        return ExportAllPointHistoryResponse(
            file = writeFilePort.writePointHistoryExcelFile(pointHistories),
            fileName = getFileName(start, endOrNow, school, pointHistories)
        )
    }

    private fun getFileName(
        start: LocalDateTime?,
        end: LocalDateTime,
        school: School,
        pointHistories: List<PointHistory>
    ): String {

        val startDate = start ?:
            if (pointHistories.isNotEmpty()) {
                pointHistories.last().createdAt
            } else end
        val startDateString = startDate.format(File.FILE_DATE_FORMAT)

        val endDateString = end.format(File.FILE_DATE_FORMAT)

        return "${school.name.replace(" ", "")}_상벌점_부여내역_${startDateString}_$endDateString"
    }
}

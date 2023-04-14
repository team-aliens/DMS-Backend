package team.aliens.dms.domain.point.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.file.model.File
import team.aliens.dms.domain.file.spi.WriteFilePort
import team.aliens.dms.domain.point.dto.ExportAllPointHistoryResponse
import team.aliens.dms.domain.point.model.PointHistory
import team.aliens.dms.domain.point.spi.QueryPointHistoryPort
import team.aliens.dms.domain.school.exception.SchoolNotFoundException
import team.aliens.dms.domain.school.model.School
import team.aliens.dms.domain.school.spi.QuerySchoolPort
import team.aliens.dms.domain.user.service.GetUserService
import java.time.LocalDateTime

@ReadOnlyUseCase
class ExportAllPointHistoryUseCase(
    private val getUserService: GetUserService,
    private val queryPointHistoryPort: QueryPointHistoryPort,
    private val querySchoolPort: QuerySchoolPort,
    private val writeFilePort: WriteFilePort
) {

    fun execute(start: LocalDateTime?, end: LocalDateTime?): ExportAllPointHistoryResponse {

        val user = getUserService.getCurrentUser()
        val school = querySchoolPort.querySchoolById(user.schoolId) ?: throw SchoolNotFoundException

        val endOrNow = end ?: LocalDateTime.now()

        val pointHistories = queryPointHistoryPort.queryPointHistoryBySchoolIdAndCreatedAtBetween(
            schoolId = user.schoolId,
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

        val startDate = start
            ?: if (pointHistories.isNotEmpty()) {
                pointHistories.last().createdAt
            } else end
        val startDateString = startDate.format(File.FILE_DATE_FORMAT)

        val endDateString = end.format(File.FILE_DATE_FORMAT)

        return "${school.name.replace(" ", "")}_상벌점_부여내역_${startDateString}_$endDateString"
    }
}

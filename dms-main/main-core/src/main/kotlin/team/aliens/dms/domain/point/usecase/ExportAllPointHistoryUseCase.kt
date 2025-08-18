package team.aliens.dms.domain.point.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.file.model.File
import team.aliens.dms.domain.file.service.FileService
import team.aliens.dms.domain.point.dto.ExportAllPointHistoryResponse
import team.aliens.dms.domain.point.model.PointHistory
import team.aliens.dms.domain.point.service.PointService
import team.aliens.dms.domain.school.model.School
import team.aliens.dms.domain.school.service.SchoolService
import team.aliens.dms.domain.user.service.UserService
import java.time.LocalDateTime

@ReadOnlyUseCase
class ExportAllPointHistoryUseCase(
    private val userService: UserService,
    private val pointService: PointService,
    private val schoolService: SchoolService,
    private val fileService: FileService
) {

    fun execute(start: LocalDateTime?, end: LocalDateTime?): ExportAllPointHistoryResponse {

        val user = userService.getCurrentUser()
        val school = schoolService.getSchoolById(user.schoolId)

        val endOrNow = end ?: LocalDateTime.now()

        val pointHistories = pointService.queryPointHistoryBySchoolIdAndCreatedAtBetween(
            schoolId = user.schoolId,
            startAt = start,
            endAt = endOrNow
        )

        return ExportAllPointHistoryResponse(
            file = fileService.writePointHistoryExcelFile(pointHistories),
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

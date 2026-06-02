package team.aliens.dms.domain.daybreak.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.common.dto.PageData
import team.aliens.dms.domain.daybreak.dto.response.ExportManagerDaybreakStudyApplicationResponse
import team.aliens.dms.domain.daybreak.service.DaybreakService
import team.aliens.dms.domain.file.service.FileService
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@ReadOnlyUseCase
class ExportManagerDaybreakStudyApplicationUseCase(
    private val daybreakService: DaybreakService,
    private val fileService: FileService,
) {
    fun execute(grade: Int?): ExportManagerDaybreakStudyApplicationResponse {
        val applications = daybreakService.managerGetDaybreakStudyApplications(grade, PageData(0, Long.MAX_VALUE))
        val file = fileService.writeDaybreakStudyApplicationExcelFile(applications)
        val fileName = "새벽자습_신청현황_${LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))}"
        return ExportManagerDaybreakStudyApplicationResponse(file = file, fileName = fileName)
    }
}

package team.aliens.dms.domain.outing.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.file.model.File
import team.aliens.dms.domain.file.service.FileService
import team.aliens.dms.domain.outing.dto.response.ExportAllOutingApplicationsResponse
import team.aliens.dms.domain.outing.service.OutingService
import team.aliens.dms.domain.school.service.SchoolService
import team.aliens.dms.domain.user.service.UserService
import java.time.LocalDate
import java.time.LocalDateTime

@ReadOnlyUseCase
class ExportAllOutingApplicationsUseCase(
    private val outingService: OutingService,
    private val fileService: FileService,
    private val userService: UserService,
    private val schoolService: SchoolService
) {

    fun execute(start: LocalDate, end: LocalDate): ExportAllOutingApplicationsResponse {
        val outingApplicationVOs = outingService.getAllOutingApplicationVOsBetweenStartAndEnd(start, end)

        val file = fileService.writeOutingApplicationExcelFile(outingApplicationVOs)

        val user = userService.getCurrentUser()
        val school = schoolService.getSchoolById(user.schoolId)

        return ExportAllOutingApplicationsResponse(
            file = file,
            fileName = getFileName(school.name)
        )
    }

    private fun getFileName(schoolName: String) =
        "${schoolName.replace(" ", "")}_외출_신청상태_${LocalDateTime.now().format(File.FILE_DATE_FORMAT)}"
}

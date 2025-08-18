package team.aliens.dms.domain.point.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.file.model.File
import team.aliens.dms.domain.file.service.FileService
import team.aliens.dms.domain.point.dto.ExportAllPointHistoryResponse
import team.aliens.dms.domain.point.service.PointService
import team.aliens.dms.domain.school.service.SchoolService
import team.aliens.dms.domain.student.service.StudentService
import team.aliens.dms.domain.user.service.UserService
import java.time.LocalDateTime

@ReadOnlyUseCase
class ExportPointHistoryUseCase(
    private val userService: UserService,
    private val pointService: PointService,
    private val schoolService: SchoolService,
    private val fileService: FileService,
    private val studentService: StudentService
) {

    fun execute(): ExportAllPointHistoryResponse {

        val user = userService.getCurrentUser()
        val school = schoolService.getSchoolById(user.schoolId)

        val students = studentService.getStudentsByNameAndSortAndFilter(
            schoolId = school.id
        )

        val pointHistory = pointService.getPointHistoryByGcnIn(
            gcns = students.map { it.gcn }
        )

        return ExportAllPointHistoryResponse(
            file = fileService.writePointHistoriesExcelFile(pointHistory, students),
            fileName = getFileName(school.name)
        )
    }

    private fun getFileName(schoolName: String) =
        "${schoolName.replace(" ", "")}_상벌점_부여내역_${LocalDateTime.now().format(File.FILE_DATE_FORMAT)}"
}

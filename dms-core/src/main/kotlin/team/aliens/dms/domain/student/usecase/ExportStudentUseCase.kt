package team.aliens.dms.domain.student.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.file.model.File
import team.aliens.dms.domain.file.service.FileService
import team.aliens.dms.domain.manager.dto.ExportStudentResponse
import team.aliens.dms.domain.school.service.SchoolService
import team.aliens.dms.domain.student.service.StudentService
import team.aliens.dms.domain.user.service.UserService
import java.time.LocalDateTime

@ReadOnlyUseCase
class ExportStudentUseCase(
    private val userService: UserService,
    private val studentService: StudentService,
    private val schoolService: SchoolService,
    private val fileService: FileService
) {

    fun execute(): ExportStudentResponse {

        val user = userService.getCurrentUser()

        val students = studentService.getStudentsBySchoolId(user.schoolId)
        val school = schoolService.getSchoolById(user.schoolId)

        return ExportStudentResponse(
            file = fileService.writeStudentExcelFile(students),
            fileName = getFileName(school.name)
        )
    }

    private fun getFileName(schoolName: String) =
        "${schoolName.replace(" ", "")}_학생목록_${LocalDateTime.now().format(File.FILE_DATE_FORMAT)}"
}

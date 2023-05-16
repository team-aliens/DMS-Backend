package team.aliens.dms.domain.remain.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.file.model.File
import team.aliens.dms.domain.file.service.FileService
import team.aliens.dms.domain.remain.dto.ExportRemainStatusResponse
import team.aliens.dms.domain.remain.dto.StudentRemainInfo
import team.aliens.dms.domain.remain.service.RemainService
import team.aliens.dms.domain.school.model.School
import team.aliens.dms.domain.school.service.SchoolService
import team.aliens.dms.domain.student.service.StudentService
import team.aliens.dms.domain.user.service.UserService
import java.time.LocalDateTime

@ReadOnlyUseCase
class ExportRemainStatusUseCase(
    private val userService: UserService,
    private val schoolService: SchoolService,
    private val studentService: StudentService,
    private val remainService: RemainService,
    private val fileService: FileService
) {

    fun execute(): ExportRemainStatusResponse {

        val user = userService.getCurrentUser()
        val school = schoolService.getSchoolById(user.schoolId)

        val students = studentService.getStudentsBySchoolId(user.schoolId)

        val remainStatusMap = remainService.getAllRemainStatusInfoByStudentId(
            studentIds = students.map { it.id }
        ).associateBy { it.studentId }

        val studentRemainInfos = students.map { student ->
            StudentRemainInfo(
                studentName = student.name,
                studentGcn = student.gcn,
                studentSex = student.sex,
                roomNumber = student.roomNumber,
                optionName = remainStatusMap[student.id]?.optionName
            )
        }

        return ExportRemainStatusResponse(
            file = fileService.writeRemainStatusExcelFile(studentRemainInfos),
            fileName = getFileName(school)
        )
    }

    private fun getFileName(school: School) =
        "${school.name.replace(" ", "")}_잔류_신청결과_${LocalDateTime.now().format(File.FILE_DATE_FORMAT)}"
}

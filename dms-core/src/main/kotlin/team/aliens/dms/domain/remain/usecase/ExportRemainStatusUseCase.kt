package team.aliens.dms.domain.remain.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.file.model.File
import team.aliens.dms.domain.file.spi.WriteFilePort
import team.aliens.dms.domain.remain.dto.StudentRemainInfo
import team.aliens.dms.domain.remain.dto.response.ExportRemainStatusResponse
import team.aliens.dms.domain.remain.spi.QueryRemainStatusPort
import team.aliens.dms.domain.school.model.School
import team.aliens.dms.domain.school.service.SchoolService
import team.aliens.dms.domain.student.spi.QueryStudentPort
import team.aliens.dms.domain.user.service.UserService
import java.time.LocalDateTime

@ReadOnlyUseCase
class ExportRemainStatusUseCase(
    private val userService: UserService,
    private val schoolService: SchoolService,
    private val queryStudentPort: QueryStudentPort,
    private val queryRemainStatusPort: QueryRemainStatusPort,
    private val writeFilePort: WriteFilePort
) {

    fun execute(): ExportRemainStatusResponse {

        val user = userService.getCurrentUser()
        val school = schoolService.getSchoolById(user.schoolId)

        val studentList = queryStudentPort.queryStudentsBySchoolId(user.schoolId)

        val remainStatusMap = queryRemainStatusPort.queryAllByStudentId(
            studentIds = studentList.map { it.id }
        ).associateBy { it.studentId }

        val studentRemainInfos = studentList.map { student ->
            StudentRemainInfo(
                studentName = student.name,
                studentGcn = student.gcn,
                studentSex = student.sex,
                roomNumber = student.roomNumber,
                optionName = remainStatusMap[student.id]?.optionName
            )
        }

        return ExportRemainStatusResponse(
            file = writeFilePort.writeRemainStatusExcelFile(studentRemainInfos),
            fileName = getFileName(school)
        )
    }

    private fun getFileName(school: School) =
        "${school.name.replace(" ", "")}_잔류_신청결과_${LocalDateTime.now().format(File.FILE_DATE_FORMAT)}"
}

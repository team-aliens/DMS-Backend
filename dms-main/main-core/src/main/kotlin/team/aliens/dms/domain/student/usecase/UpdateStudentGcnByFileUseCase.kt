package team.aliens.dms.domain.student.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.file.service.FileService
import team.aliens.dms.domain.student.service.StudentService
import team.aliens.dms.domain.user.service.UserService

@UseCase
class UpdateStudentGcnByFileUseCase(
    private val userService: UserService,
    private val studentService: StudentService,
    private val fileService: FileService
) {

    fun execute(file: java.io.File) {
        val user = userService.getCurrentUser()
        val parsedStudentVOs = fileService.getExcelStudentVO(file)

        val studentMap = studentService.getStudentsByRoomNumberAndRoomLocationIn(
            schoolId = user.schoolId,
            roomNumberLocations = parsedStudentVOs.map { it.pairRoomNumberAndLocation }
        ).associateBy { Pair(it.roomNumber, it.roomLocation) }

        studentService.saveAllStudent(
            students = studentService.getGcnUpdatedStudent(
                studentMap = studentMap,
                studentVOs = parsedStudentVOs
            )
        )
    }
}

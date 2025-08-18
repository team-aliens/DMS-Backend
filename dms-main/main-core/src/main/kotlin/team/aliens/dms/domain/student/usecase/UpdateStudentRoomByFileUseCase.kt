package team.aliens.dms.domain.student.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.file.service.FileService
import team.aliens.dms.domain.room.service.RoomService
import team.aliens.dms.domain.student.service.StudentService
import team.aliens.dms.domain.user.service.UserService

@UseCase
class UpdateStudentRoomByFileUseCase(
    private val userService: UserService,
    private val studentService: StudentService,
    private val roomService: RoomService,
    private val fileService: FileService,
) {

    fun execute(file: java.io.File) {
        val user = userService.getCurrentUser()
        val parsedStudentVOs = fileService.getExcelStudentVO(file)

        val roomMap = roomService.saveNotExistsRooms(
            schoolId = user.schoolId,
            roomNumbers = parsedStudentVOs.map { it.roomNumber }.distinctBy { it }
        )

        val studentMap = studentService.getStudentsBySchoolIdAndGcnIn(
            schoolId = user.schoolId,
            gcnList = parsedStudentVOs.map { it.tripleGcn }
        ).associateBy { Triple(it.grade, it.classRoom, it.number) }

        studentService.saveAllStudent(
            students = studentService.getRoomUpdatedStudent(
                roomMap = roomMap,
                studentMap = studentMap,
                studentVOs = parsedStudentVOs
            )
        )
    }
}

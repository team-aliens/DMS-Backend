package team.aliens.dms.domain.file.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.file.service.FileService
import team.aliens.dms.domain.room.service.RoomService
import team.aliens.dms.domain.school.service.SchoolService
import team.aliens.dms.domain.student.model.Student
import team.aliens.dms.domain.student.service.StudentService
import team.aliens.dms.domain.user.service.UserService
import java.io.File

@UseCase
class ImportStudentUseCase(
    private val userService: UserService,
    private val studentService: StudentService,
    private val roomService: RoomService,
    private val fileService: FileService,
    private val schoolService: SchoolService
) {

    fun execute(file: File) {
        val user = userService.getCurrentUser()
        val school = schoolService.getSchoolById(user.schoolId)

        val parsedStudentInfos = fileService.getExcelStudentVO(file)

        val gcnList = parsedStudentInfos.map { Triple(it.grade, it.classRoom, it.number) }
        studentService.checkStudentExistsBySchoolIdAndGcnList(school.id, gcnList)

        val roomMap = roomService.saveNotExistsRooms(
            roomNumbers = parsedStudentInfos.map { it.roomNumber }.distinctBy { it },
            schoolId = school.id
        )

        studentService.saveAllStudent(
            parsedStudentInfos.map {
                Student(
                    roomId = roomMap[it.roomNumber]!!.id,
                    roomNumber = it.roomNumber,
                    roomLocation = it.roomLocation,
                    schoolId = school.id,
                    grade = it.grade,
                    classRoom = it.classRoom,
                    number = it.number,
                    sex = it.sex,
                    name = it.name
                )
            }
        )
    }
}

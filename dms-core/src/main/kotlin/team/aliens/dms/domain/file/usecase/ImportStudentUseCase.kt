package team.aliens.dms.domain.file.usecase

import java.io.File
import java.util.UUID
import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.file.spi.ParseFilePort
import team.aliens.dms.domain.room.model.Room
import team.aliens.dms.domain.room.spi.FileCommandRoomPort
import team.aliens.dms.domain.room.spi.FileQueryRoomPort
import team.aliens.dms.domain.school.exception.SchoolNotFoundException
import team.aliens.dms.domain.school.spi.QuerySchoolPort
import team.aliens.dms.domain.student.model.Student
import team.aliens.dms.domain.student.service.StudentService
import team.aliens.dms.domain.user.service.UserService

@UseCase
class ImportStudentUseCase(
    private val userService: UserService,
    private val studentService: StudentService,
    private val parseFilePort: ParseFilePort,
    private val querySchoolPort: QuerySchoolPort,
    private val fileCommandRoomPort: FileCommandRoomPort,
    private val fileQueryRoomPort: FileQueryRoomPort
) {

    fun execute(file: File) {
        val user = userService.getCurrentUser()
        val school = querySchoolPort.querySchoolById(user.schoolId) ?: throw SchoolNotFoundException

        val parsedStudentInfos = parseFilePort.getExcelStudentVO(file)

        val gcnList = parsedStudentInfos.map { Triple(it.grade, it.classRoom, it.number) }
        studentService.checkStudentExistsBySchoolIdAndGcnList(school.id, gcnList)

        val roomMap = saveNotExistsRooms(
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

    private fun saveNotExistsRooms(roomNumbers: List<String>, schoolId: UUID): MutableMap<String, Room> {

        val roomMap = fileQueryRoomPort.queryRoomsByRoomNumbersIn(roomNumbers, schoolId)
            .associateBy { it.number }
            .toMutableMap()

        val notExistsRooms = roomNumbers.mapNotNull { roomNumber ->
            if (!roomMap.containsKey(roomNumber)) {
                Room(
                    number = roomNumber,
                    schoolId = schoolId
                ).apply {
                    roomMap[roomNumber] = this
                }
            } else {
                null
            }
        }

        fileCommandRoomPort.saveRooms(notExistsRooms)
        return roomMap
    }
}

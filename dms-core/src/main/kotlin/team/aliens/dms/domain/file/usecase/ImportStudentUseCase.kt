package team.aliens.dms.domain.file.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.file.spi.FileQueryRoomPort
import team.aliens.dms.domain.file.spi.FileQuerySchoolPort
import team.aliens.dms.domain.file.spi.FileQueryUserPort
import team.aliens.dms.domain.file.spi.FileSecurityPort
import team.aliens.dms.domain.file.spi.ParseFilePort
import team.aliens.dms.domain.room.model.Room
import team.aliens.dms.domain.room.spi.RoomPort
import team.aliens.dms.domain.school.exception.SchoolNotFoundException
import team.aliens.dms.domain.student.exception.StudentAlreadyExistsException
import team.aliens.dms.domain.student.model.Student
import team.aliens.dms.domain.student.spi.CommandStudentPort
import team.aliens.dms.domain.student.spi.QueryStudentPort
import team.aliens.dms.domain.user.exception.UserNotFoundException
import java.io.File
import java.util.UUID

@UseCase
class ImportStudentUseCase(
    private val parseFilePort: ParseFilePort,
    private val securityPort: FileSecurityPort,
    private val queryUserPort: FileQueryUserPort,
    private val querySchoolPort: FileQuerySchoolPort,
    private val commandRoomPort: RoomPort,
    private val queryRoomPort: FileQueryRoomPort,
    private val commandStudentPort: CommandStudentPort,
    private val queryStudentPort: QueryStudentPort
) {

    fun execute(file: File) {
        val currentUserId = securityPort.getCurrentUserId()
        val user = queryUserPort.queryUserById(currentUserId) ?: throw UserNotFoundException
        val school = querySchoolPort.querySchoolById(user.schoolId) ?: throw SchoolNotFoundException

        val parsedStudentInfos = parseFilePort.getExcelStudentVO(file)

        val gcnList = parsedStudentInfos.map { Triple(it.grade, it.classRoom, it.number) }
        if (queryStudentPort.existsBySchoolIdAndGcnList(school.id, gcnList)) {
            throw StudentAlreadyExistsException
        }

        val roomMap = saveNotExistsRooms(
            roomNumbers = parsedStudentInfos.map { it.roomNumber }.distinctBy { it },
            schoolId = school.id
        )

        commandStudentPort.saveAllStudent(
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

        val roomMap = queryRoomPort.queryRoomsByRoomNumbersIn(roomNumbers, schoolId)
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

        commandRoomPort.saveRooms(notExistsRooms)
        return roomMap
    }
}

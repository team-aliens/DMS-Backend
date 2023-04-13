package team.aliens.dms.domain.file.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.common.spi.SecurityPort
import team.aliens.dms.domain.file.spi.ParseFilePort
import team.aliens.dms.domain.room.model.Room
import team.aliens.dms.domain.room.spi.CommandRoomPort
import team.aliens.dms.domain.room.spi.QueryRoomPort
import team.aliens.dms.domain.school.exception.SchoolNotFoundException
<<<<<<< develop:dms-core/src/main/kotlin/team/aliens/dms/domain/file/usecase/ImportStudentUseCase.kt
=======
import team.aliens.dms.domain.school.model.School
import team.aliens.dms.domain.school.spi.QuerySchoolPort
>>>>>>> refactor: (#441) 포트 분리:dms-core/src/main/kotlin/team/aliens/dms/domain/file/usecase/ImportVerifiedStudentUseCase.kt
import team.aliens.dms.domain.student.exception.StudentAlreadyExistsException
import team.aliens.dms.domain.student.model.Student
import team.aliens.dms.domain.student.spi.CommandStudentPort
import team.aliens.dms.domain.student.spi.QueryStudentPort
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.spi.QueryUserPort
import java.io.File
import java.util.UUID

@UseCase
class ImportStudentUseCase(
    private val parseFilePort: ParseFilePort,
    private val securityPort: SecurityPort,
    private val queryUserPort: QueryUserPort,
    private val querySchoolPort: QuerySchoolPort,
    private val commandRoomPort: CommandRoomPort,
    private val queryRoomPort: QueryRoomPort,
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

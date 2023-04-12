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
import team.aliens.dms.domain.school.model.School
import team.aliens.dms.domain.student.exception.StudentAlreadyExistsException
import team.aliens.dms.domain.student.model.VerifiedStudent
import team.aliens.dms.domain.student.spi.CommandStudentPort
import team.aliens.dms.domain.student.spi.QueryStudentPort
import team.aliens.dms.domain.user.exception.UserNotFoundException
import java.io.File

@UseCase
class ImportVerifiedStudentUseCase(
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

        val verifiedStudents = parseFilePort.transferToVerifiedStudent(file, school.name)
        val gcnList = verifiedStudents
            .map {
                it.calculateEachGcn()
            }

        if (queryStudentPort.existsBySchoolIdAndGcnList(school.id, gcnList)) {
            throw StudentAlreadyExistsException
        }

        saveNotExistsRooms(verifiedStudents, school)

        commandStudentPort.saveAllVerifiedStudent(verifiedStudents)
    }

    private fun saveNotExistsRooms(verifiedStudents: List<VerifiedStudent>, school: School) {
        val roomNumbers = verifiedStudents.map { it.roomNumber }.distinctBy { it }
        val existsRoomsMap = queryRoomPort.queryRoomsByRoomNumbersIn(roomNumbers, school.id).associateBy { it.number }

        val notExistsRooms = roomNumbers.mapNotNull {
            if (!existsRoomsMap.containsKey(it)) {
                Room(
                    number = it,
                    schoolId = school.id
                )
            } else {
                null
            }
        }

        commandRoomPort.saveRooms(notExistsRooms)
    }
}

package team.aliens.dms.domain.student.service

import java.util.UUID
import team.aliens.dms.common.annotation.Service
import team.aliens.dms.common.spi.SecurityPort
import team.aliens.dms.domain.manager.dto.PointFilter
import team.aliens.dms.domain.manager.dto.Sort
import team.aliens.dms.domain.student.exception.StudentNotFoundException
import team.aliens.dms.domain.student.model.Student
import team.aliens.dms.domain.student.spi.QueryStudentPort

@Service
class GetStudentServiceImpl(
    private val securityPort: SecurityPort,
    private val queryStudentPort: QueryStudentPort
) : GetStudentService {

    override fun getCurrentStudent(): Student {
        val currentUserId = securityPort.getCurrentUserId()
        return queryStudentPort.queryStudentById(currentUserId) ?: throw StudentNotFoundException
    }

    override fun queryStudentBySchoolIdAndGcn(schoolId: UUID, grade: Int, classRoom: Int, number: Int) =
        queryStudentPort.queryStudentBySchoolIdAndGcn(schoolId, grade, classRoom, number)
            ?: throw StudentNotFoundException

    override fun queryStudentById(studentId: UUID) =
        queryStudentPort.queryStudentById(studentId) ?: throw StudentNotFoundException

    override fun queryStudentByUserId(userId: UUID) =
        queryStudentPort.queryStudentByUserId(userId) ?: throw StudentNotFoundException

    override fun queryStudentsByNameAndSortAndFilter(
        name: String?,
        sort: Sort,
        schoolId: UUID,
        pointFilter: PointFilter,
        tagIds: List<UUID>?,
    ) = queryStudentPort.queryStudentsByNameAndSortAndFilter(name, sort, schoolId, pointFilter, tagIds)

    override fun getRoommates(studentId: UUID, roomNumber: String, schoolId: UUID): List<Student> {
        return queryStudentPort.queryStudentsByRoomNumberAndSchoolId(roomNumber, schoolId)
            .filter {
                it.id != studentId
            }
    }

    override fun queryStudentsWithPointHistory(studentIds: List<UUID>) =
        queryStudentPort.queryStudentsWithPointHistory(studentIds)

    override fun queryStudentsBySchoolId(schoolId: UUID) =
        queryStudentPort.queryStudentsBySchoolId(schoolId)

    override fun queryAllStudentsByIdsIn(studentIds: List<UUID>) =
        queryStudentPort.queryAllStudentsByIdsIn(studentIds)
            .also { students ->
                if (!students.map { it.id }.containsAll(studentIds)) {
                    throw StudentNotFoundException
                }
            }
}

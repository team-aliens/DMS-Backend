package team.aliens.dms.domain.student.service

import java.util.UUID
import team.aliens.dms.domain.manager.dto.PointFilter
import team.aliens.dms.domain.manager.dto.Sort
import team.aliens.dms.domain.manager.spi.vo.StudentWithTag
import team.aliens.dms.domain.point.spi.vo.StudentWithPointVO
import team.aliens.dms.domain.student.model.Student

interface GetStudentService {

    fun getCurrentStudent(): Student

    fun getStudentBySchoolIdAndGcn(schoolId: UUID, grade: Int, classRoom: Int, number: Int): Student

    fun getStudentById(studentId: UUID): Student

    fun getStudentByUserId(userId: UUID): Student

    fun getStudentsByNameAndSortAndFilter(
        name: String?,
        sort: Sort,
        schoolId: UUID,
        pointFilter: PointFilter,
        tagIds: List<UUID>?
    ): List<StudentWithTag>

    fun getRoommates(studentId: UUID, roomNumber: String, schoolId: UUID): List<Student>

    fun getStudentsWithPointHistory(studentIds: List<UUID>): List<StudentWithPointVO>

    fun getStudentsBySchoolId(schoolId: UUID): List<Student>

    fun getAllStudentsByIdsIn(studentIds: List<UUID>): List<Student>
}

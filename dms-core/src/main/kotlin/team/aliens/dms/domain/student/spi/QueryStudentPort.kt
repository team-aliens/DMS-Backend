package team.aliens.dms.domain.student.spi

import team.aliens.dms.domain.manager.dto.PointFilter
import team.aliens.dms.domain.manager.dto.Sort
import team.aliens.dms.domain.manager.spi.vo.StudentWithTag
import team.aliens.dms.domain.point.spi.vo.StudentWithPointVO
import team.aliens.dms.domain.student.model.Student
import java.util.UUID

interface QueryStudentPort {

    fun queryStudentBySchoolIdAndGcn(schoolId: UUID, grade: Int, classRoom: Int, number: Int): Student?

    fun queryStudentById(studentId: UUID): Student?

    fun queryStudentByUserId(userId: UUID): Student?

    fun existsStudentByUserId(studentId: UUID): Boolean

    fun existsBySchoolIdAndGcnList(schoolId: UUID, gcnList: List<Triple<Int, Int, Int>>): Boolean

    fun queryStudentsByNameAndSortAndFilter(
        name: String?,
        sort: Sort,
        schoolId: UUID,
        pointFilter: PointFilter,
        tagIds: List<UUID>?
    ): List<StudentWithTag>

    fun queryStudentsByRoomNumberAndSchoolId(roomNumber: String, schoolId: UUID): List<Student>

    fun queryStudentsWithPointHistory(studentIds: List<UUID>): List<StudentWithPointVO>

    fun queryStudentsBySchoolId(schoolId: UUID): List<Student>

    fun queryAllStudentsByIdsIn(studentIds: List<UUID>): List<Student>
}

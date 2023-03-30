package team.aliens.dms.domain.manager.spi

import team.aliens.dms.domain.manager.dto.PointFilter
import team.aliens.dms.domain.manager.dto.Sort
import team.aliens.dms.domain.manager.spi.vo.StudentWithTag
import team.aliens.dms.domain.student.model.Student
import java.util.UUID

interface ManagerQueryStudentPort {

    fun queryStudentById(studentId: UUID): Student?

    fun queryStudentsByNameAndSortAndFilter(
        name: String?,
        sort: Sort,
        schoolId: UUID,
        pointFilter: PointFilter,
        tagIds: List<UUID>?
    ): List<StudentWithTag>

    fun queryUserByRoomNumberAndSchoolId(roomNumber: String, schoolId: UUID): List<Student>
}

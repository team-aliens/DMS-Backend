package team.aliens.dms.domain.manager.spi

import team.aliens.dms.domain.manager.dto.StudentPointFilter
import team.aliens.dms.domain.manager.dto.Sort
import team.aliens.dms.domain.student.model.Student
import java.util.UUID

interface ManagerQueryStudentPort {

    fun queryStudentById(studentId: UUID): Student?

    fun queryStudentsByNameAndSortAndFilter(
        name: String?,
        sort: Sort,
        schoolId: UUID,
        pointFilter: StudentPointFilter?
    ): List<Student>

    fun queryUserByRoomNumberAndSchoolId(roomNumber: Int, schoolId: UUID): List<Student>

}
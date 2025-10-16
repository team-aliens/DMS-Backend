package team.aliens.dms.domain.student.service

import team.aliens.dms.domain.file.spi.vo.ExcelStudentVO
import team.aliens.dms.domain.manager.dto.PointFilter
import team.aliens.dms.domain.manager.dto.Sort
import team.aliens.dms.domain.manager.spi.vo.StudentWithTag
import team.aliens.dms.domain.point.spi.vo.StudentWithPointVO
import team.aliens.dms.domain.room.model.Room
import team.aliens.dms.domain.student.model.Student
import team.aliens.dms.domain.student.spi.vo.AllStudentsVO
import team.aliens.dms.domain.student.spi.vo.ModelStudentVO
import java.time.LocalDate
import java.util.UUID

interface GetStudentService {

    fun getCurrentStudent(): Student

    fun getStudentBySchoolIdAndGcn(schoolId: UUID, grade: Int, classRoom: Int, number: Int): Student

    fun getStudentsBySchoolIdAndGcnIn(schoolId: UUID, gcnList: List<Triple<Int, Int, Int>>): List<Student>

    fun getStudentsByRoomNumberAndRoomLocationIn(schoolId: UUID, roomNumberLocations: List<Pair<String, String>>): List<Student>

    fun getStudentById(studentId: UUID): Student

    fun getStudentByUserId(userId: UUID): Student

    fun getStudentsByNameAndSortAndFilter(
        name: String? = null,
        sort: Sort = Sort.GCN,
        schoolId: UUID,
        pointFilter: PointFilter = PointFilter(null, null, null),
        tagIds: List<UUID>? = null
    ): List<StudentWithTag>

    fun getRoommates(studentId: UUID, roomNumber: String, schoolId: UUID): List<Student>

    fun getStudentsWithPointHistory(studentIds: List<UUID>): List<StudentWithPointVO>

    fun getStudentsBySchoolId(schoolId: UUID): List<Student>

    fun getAllStudentsByIdsIn(studentIds: List<UUID>): List<Student>

    fun getGcnUpdatedStudent(
        studentMap: Map<Pair<String, String>, Student>,
        studentVOs: List<ExcelStudentVO>
    ): List<Student>

    fun getRoomUpdatedStudent(
        roomMap: Map<String, Room>,
        studentMap: Map<Triple<Int, Int, Int>, Student>,
        studentVOs: List<ExcelStudentVO>,
    ): List<Student>

    fun getAllStudentsByName(name: String?, schoolId: UUID): List<AllStudentsVO>

    fun isApplicant(studentId: UUID): Boolean

    fun getModelStudents(date: LocalDate, schoolId: UUID): List<ModelStudentVO>
}

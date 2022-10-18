package team.aliens.dms.persistence.student

import org.springframework.stereotype.Component
import team.aliens.dms.domain.student.model.Student
import team.aliens.dms.domain.student.spi.StudentPort
import team.aliens.dms.persistence.student.mapper.StudentMapper
import team.aliens.dms.persistence.student.repository.StudentJpaRepository
import java.util.*

@Component
class StudentPersistenceAdapter(
    private val studentMapper: StudentMapper,
    private val studentRepository: StudentJpaRepository
) : StudentPort {

    override fun existsByGcn(grade: Int, classRoom: Int, number: Int): Boolean =
        studentRepository.existsByGradeAndClassRoomAndNumber(grade, classRoom, number)

    override fun queryStudentBySchoolIdAndGcn(schoolId: UUID, grade: Int, classRoom: Int, number: Int): Student? {
        val student = studentRepository.findByUserSchoolIdAndGradeAndClassRoomAndNumber(schoolId, grade, classRoom, number)
        return studentMapper.toDomain(student)
    }
}
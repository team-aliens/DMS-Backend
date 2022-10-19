package team.aliens.dms.persistence.student

import org.springframework.stereotype.Component
import team.aliens.dms.domain.student.spi.StudentPort
import team.aliens.dms.persistence.student.mapper.StudentMapper
import team.aliens.dms.persistence.student.repository.StudentJpaRepository
import java.util.*

@Component
class StudentPersistenceAdapter(
    private val studentMapper: StudentMapper,
    private val studentRepository: StudentJpaRepository
) : StudentPort {

    override fun queryStudentBySchoolIdAndGcn(schoolId: UUID, grade: Int, classRoom: Int, number: Int) = studentMapper.toDomain(
        studentRepository.findByUserSchoolIdAndGradeAndClassRoomAndNumber(schoolId, grade, classRoom, number)
    )
}
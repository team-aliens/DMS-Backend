package team.aliens.dms.persistence.student

import org.springframework.data.repository.findByIdOrNull
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

    override fun queryStudentBySchoolIdAndGcn(schoolId: UUID, grade: Int, classRoom: Int, number: Int) =
        studentMapper.toDomain(
            studentRepository.findByUserSchoolIdAndGradeAndClassRoomAndNumber(schoolId, grade, classRoom, number)
        )

    override fun saveStudent(student: Student) = studentMapper.toDomain(
        studentRepository.save(
            studentMapper.toEntity(student)
        )
    )!!

    override fun queryStudentById(studentId: UUID) = studentMapper.toDomain(
        studentRepository.findByIdOrNull(studentId)
    )
    
    override fun queryByUserId(userId: UUID) = studentMapper.toDomain(
        studentRepository.findByIdOrNull(userId)
    )
}
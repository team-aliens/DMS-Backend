package team.aliens.dms.persistence.teacher

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.teacher.spi.TeacherPort
import team.aliens.dms.persistence.teacher.mapper.TeacherMapper
import team.aliens.dms.persistence.teacher.repository.TeacherJpaRepository
import java.util.UUID

@Component
class TeacherPersistenceAdapter(
    private val teacherRepository: TeacherJpaRepository,
    private val teacherMapper: TeacherMapper
) : TeacherPort {

    override fun getTeacherById(id: UUID) =
        teacherMapper.toDomain(teacherRepository.findByIdOrNull(id))
}

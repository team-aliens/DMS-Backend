package team.aliens.dms.persistence.teacher

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.teacher.spi.TeacherPort
import team.aliens.dms.persistence.teacher.repository.TeacherJpaRepository
import java.util.UUID

@Component
class TeacherPersistenceAdapter(
    private val teacherRepository: TeacherJpaRepository
) : TeacherPort {

    override fun getTeacherRoleById(id: UUID) = teacherRepository.findByIdOrNull(id)!!.role
}

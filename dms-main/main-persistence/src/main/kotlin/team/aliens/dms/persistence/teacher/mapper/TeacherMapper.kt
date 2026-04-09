package team.aliens.dms.persistence.teacher.mapper

import jakarta.persistence.EntityManager
import org.springframework.data.repository.findByIdOrNull
import team.aliens.dms.domain.teacher.model.Teacher
import team.aliens.dms.persistence.GenericMapper
import team.aliens.dms.persistence.teacher.entity.TeacherJpaEntity
import team.aliens.dms.persistence.user.entity.UserJpaEntity

class TeacherMapper(
    private val entityManager: EntityManager,
): GenericMapper<Teacher, TeacherJpaEntity> {

    override fun toDomain(entity: TeacherJpaEntity?): Teacher? {
        return entity?.let {
            Teacher(
                id = entity.id,
                name = entity.name,
                role = entity.role
            )
        }
    }

    override fun toEntity(domain: Teacher): TeacherJpaEntity {

        return TeacherJpaEntity(
            id = domain.id,
            name = domain.name,
            role = domain.role,
            user = entityManager.getReference(UserJpaEntity::class.java, domain.id)
        )
    }
}

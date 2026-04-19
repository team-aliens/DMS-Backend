package team.aliens.dms.persistence.teacher.mapper

import jakarta.persistence.EntityManager
import org.springframework.stereotype.Component
import team.aliens.dms.domain.teacher.model.Teacher
import team.aliens.dms.persistence.GenericMapper
import team.aliens.dms.persistence.teacher.entity.TeacherJpaEntity
import team.aliens.dms.persistence.user.entity.UserJpaEntity

@Component
class TeacherMapper(
    private val entityManager: EntityManager,
) : GenericMapper<Teacher, TeacherJpaEntity> {

    override fun toDomain(entity: TeacherJpaEntity?): Teacher? {
        return entity?.let {
            Teacher(
                id = entity.id,
                name = entity.name,
                grade = entity.grade,
            )
        }
    }

    override fun toEntity(domain: Teacher): TeacherJpaEntity {

        return TeacherJpaEntity(
            id = domain.id,
            name = domain.name,
            user = entityManager.getReference(UserJpaEntity::class.java, domain.id),
            grade = domain.grade
        )
    }
}

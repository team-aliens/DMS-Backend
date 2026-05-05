package team.aliens.dms.persistence.daybreak.mapper

import jakarta.persistence.EntityManager
import org.springframework.stereotype.Component
import team.aliens.dms.domain.daybreak.model.DaybreakStudyApplication
import team.aliens.dms.persistence.GenericMapper
import team.aliens.dms.persistence.daybreak.entity.DaybreakStudyApplicationJpaEntity
import team.aliens.dms.persistence.daybreak.entity.DaybreakStudyTypeJpaEntity
import team.aliens.dms.persistence.school.entity.SchoolJpaEntity
import team.aliens.dms.persistence.student.entity.StudentJpaEntity
import team.aliens.dms.persistence.teacher.entity.TeacherJpaEntity

@Component
class DaybreakStudyApplicationMapper(
    private val entityManager: EntityManager,
    private val daybreakStudyTypeMapper: DaybreakStudyTypeMapper
) : GenericMapper<DaybreakStudyApplication, DaybreakStudyApplicationJpaEntity> {

    override fun toDomain(entity: DaybreakStudyApplicationJpaEntity?): DaybreakStudyApplication? {
        return entity?.let {
            DaybreakStudyApplication(
                id = it.id!!,
                studyTypeId = daybreakStudyTypeMapper.toDomain(it.daybreakStudyTypeJpaEntity)!!.id,
                startDate = it.startDate,
                endDate = it.endDate,
                reason = it.reason,
                status = it.status,
                teacherId = it.teacherJpaEntity.id,
                studentId = it.studentJpaEntity.id!!,
                schoolId = it.schoolJpaEntity.id!!
            )
        }
    }

    override fun toEntity(domain: DaybreakStudyApplication): DaybreakStudyApplicationJpaEntity {
        return DaybreakStudyApplicationJpaEntity(
            id = domain.id,
            startDate = domain.startDate,
            endDate = domain.endDate,
            reason = domain.reason,
            status = domain.status,
            daybreakStudyTypeJpaEntity = entityManager.getReference(DaybreakStudyTypeJpaEntity::class.java, domain.studyTypeId),
            studentJpaEntity = entityManager.getReference(StudentJpaEntity::class.java, domain.studentId),
            teacherJpaEntity = entityManager.getReference(TeacherJpaEntity::class.java, domain.teacherId),
            schoolJpaEntity = entityManager.getReference(SchoolJpaEntity::class.java, domain.schoolId)
        )
    }
}

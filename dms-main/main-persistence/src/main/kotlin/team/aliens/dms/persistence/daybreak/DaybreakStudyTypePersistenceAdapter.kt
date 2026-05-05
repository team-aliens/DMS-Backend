package team.aliens.dms.persistence.daybreak

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.daybreak.model.DaybreakStudyType
import team.aliens.dms.domain.daybreak.spi.DaybreakStudyTypePort
import team.aliens.dms.persistence.daybreak.entity.QDaybreakStudyTypeJpaEntity.daybreakStudyTypeJpaEntity
import team.aliens.dms.persistence.daybreak.mapper.DaybreakStudyTypeMapper
import team.aliens.dms.persistence.daybreak.repository.DaybreakStudyTypeJpaRepository
import java.util.UUID

@Component
class DaybreakStudyTypePersistenceAdapter(
    private val daybreakStudyTypeRepository: DaybreakStudyTypeJpaRepository,
    private val daybreakStudyTypeMapper: DaybreakStudyTypeMapper,
    private val queryFactory: JPAQueryFactory
) : DaybreakStudyTypePort {

    override fun saveDaybreakStudyType(type: DaybreakStudyType) {
        daybreakStudyTypeRepository.save(daybreakStudyTypeMapper.toEntity(type))
    }

    override fun daybreakStudyTypeById(id: UUID): DaybreakStudyType? {
        return daybreakStudyTypeMapper.toDomain(daybreakStudyTypeRepository.findByIdOrNull(id))
    }

    override fun daybreakStudyTypesBySchoolId(id: UUID): List<DaybreakStudyType> {
        return daybreakStudyTypeRepository.findAllBySchoolJpaEntityId(id).mapNotNull {
            daybreakStudyTypeMapper.toDomain(it)
        }
    }

    override fun existsDaybreakStudyTypeBySchoolIdAndName(schoolId: UUID, name: String): Boolean {
        val fetchOne = queryFactory
            .selectOne()
            .from(daybreakStudyTypeJpaEntity)
            .where(
                daybreakStudyTypeJpaEntity.schoolJpaEntity.id.eq(schoolId),
                daybreakStudyTypeJpaEntity.name.eq(name)
            )
            .fetchFirst()

        return fetchOne != null
    }
}

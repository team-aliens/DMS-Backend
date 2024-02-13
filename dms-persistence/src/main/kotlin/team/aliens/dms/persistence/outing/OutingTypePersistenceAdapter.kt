package team.aliens.dms.persistence.outing

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.outing.model.OutingType
import team.aliens.dms.domain.outing.spi.OutingTypePort
import team.aliens.dms.persistence.outing.entity.OutingTypeJpaEntityId
import team.aliens.dms.persistence.outing.entity.QOutingTypeJpaEntity.outingTypeJpaEntity
import team.aliens.dms.persistence.outing.mapper.OutingTypeMapper
import team.aliens.dms.persistence.outing.repository.OutingTypeJpaRepository
import java.util.UUID

@Component
class OutingTypePersistenceAdapter(
    private val outingTypeRepository: OutingTypeJpaRepository,
    private val outingTypeMapper: OutingTypeMapper,
    private val queryFactory: JPAQueryFactory
) : OutingTypePort {

    override fun existsOutingType(outingType: OutingType): Boolean {
        val id = OutingTypeJpaEntityId(outingType.title, outingType.schoolId)
        return outingTypeRepository.existsById(id)
    }

    override fun queryOutingType(outingType: OutingType): OutingType? {
        val id = OutingTypeJpaEntityId(outingType.title, outingType.schoolId)
        return outingTypeMapper.toDomain(
            outingTypeRepository.findByIdOrNull(id)
        )
    }

    override fun queryAllOutingTypeTitlesBySchoolId(schoolId: UUID): List<String> =
        queryFactory
            .select(outingTypeJpaEntity.id.title)
            .from(outingTypeJpaEntity)
            .where(outingTypeJpaEntity.id.schoolId.eq(schoolId))
            .fetch()

    override fun saveOutingType(outingType: OutingType): OutingType =
        outingTypeMapper.toDomain(
            outingTypeRepository.save(
                outingTypeMapper.toEntity(outingType)
            )
        )!!

    override fun deleteOutingType(outingType: OutingType) {
        outingTypeRepository.delete(
            outingTypeMapper.toEntity(outingType)
        )
    }
}

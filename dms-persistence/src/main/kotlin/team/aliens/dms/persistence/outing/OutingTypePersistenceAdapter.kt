package team.aliens.dms.persistence.outing

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.outing.model.OutingType
import team.aliens.dms.domain.outing.spi.OutingTypePort
import team.aliens.dms.persistence.outing.entity.OutingTypeJpaEntityId
import team.aliens.dms.persistence.outing.mapper.OutingTypeMapper
import team.aliens.dms.persistence.outing.repository.OutingTypeJpaRepository

@Component
class OutingTypePersistenceAdapter(
    private val outingTypeRepository: OutingTypeJpaRepository,
    private val outingTypeMapper: OutingTypeMapper
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

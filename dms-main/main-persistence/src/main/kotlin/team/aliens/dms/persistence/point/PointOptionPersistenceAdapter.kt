package team.aliens.dms.persistence.point

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.point.model.PointOption
import team.aliens.dms.domain.point.spi.PointOptionPort
import team.aliens.dms.persistence.point.mapper.PointOptionMapper
import team.aliens.dms.persistence.point.repository.PointOptionJpaRepository
import java.util.UUID

@Component
class PointOptionPersistenceAdapter(
    private val queryFactory: JPAQueryFactory,
    private val pointOptionMapper: PointOptionMapper,
    private val pointOptionRepository: PointOptionJpaRepository
) : PointOptionPort {
    override fun existByNameAndSchoolId(name: String, schoolId: UUID) =
        pointOptionRepository.existsByNameAndSchoolId(name, schoolId)

    override fun savePointOption(pointOption: PointOption): PointOption {
        return pointOptionMapper.toDomain(
            pointOptionRepository.save(pointOptionMapper.toEntity(pointOption))
        )!!
    }

    override fun deletePointOption(pointOption: PointOption) {
        pointOptionRepository.delete(
            pointOptionMapper.toEntity(pointOption)
        )
    }

    override fun queryPointOptionById(pointOptionId: UUID) = pointOptionMapper.toDomain(
        pointOptionRepository.findByIdOrNull(pointOptionId)
    )

    override fun queryPointOptionsBySchoolIdAndKeyword(schoolId: UUID, keyword: String?): List<PointOption> {
        return pointOptionRepository.findBySchoolIdAndNameContains(schoolId, keyword ?: "")
            .map {
                pointOptionMapper.toDomain(it)!!
            }
            .sortedBy { it.createdAt }
    }
}

package team.aliens.dms.persistence.remain

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.remain.model.RemainOption
import team.aliens.dms.domain.remain.spi.RemainOptionPort
import team.aliens.dms.persistence.remain.mapper.RemainOptionMapper
import team.aliens.dms.persistence.remain.repository.RemainOptionJpaRepository
import java.util.UUID

@Component
class RemainOptionPersistenceAdapter(
    private val remainOptionRepository: RemainOptionJpaRepository,
    private val remainOptionMapper: RemainOptionMapper
) : RemainOptionPort {

    override fun saveRemainOption(remainOption: RemainOption) = remainOptionMapper.toDomain(
        remainOptionRepository.save(
            remainOptionMapper.toEntity(remainOption)
        )
    )!!

    override fun queryRemainOptionById(remainOptionId: UUID) = remainOptionMapper.toDomain(
        remainOptionRepository.findByIdOrNull(remainOptionId)
    )

    override fun queryAllRemainOptionsBySchoolId(schoolId: UUID) =
        remainOptionRepository.findAllBySchoolId(schoolId)
            .map {
                remainOptionMapper.toDomain(it)!!
            }
}
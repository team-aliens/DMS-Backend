package team.aliens.dms.persistence.remain

import org.springframework.stereotype.Component
import team.aliens.dms.domain.remain.model.RemainOption
import team.aliens.dms.domain.remain.spi.RemainOptionPort
import team.aliens.dms.persistence.remain.mapper.RemainOptionMapper
import team.aliens.dms.persistence.remain.repository.RemainOptionJpaRepository

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
}
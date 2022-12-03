package team.aliens.dms.persistence.point

import org.springframework.stereotype.Component
import team.aliens.dms.domain.point.model.Phrase
import team.aliens.dms.domain.point.model.PointType
import team.aliens.dms.domain.point.spi.PhrasePort
import team.aliens.dms.persistence.point.mapper.PhraseMapper
import team.aliens.dms.persistence.point.repository.PhraseJpaRepository

@Component
class PhrasePersistenceAdapterStudent(
    private val phraseRepository: PhraseJpaRepository,
    private val phraseMapper: PhraseMapper
) : PhrasePort {

    override fun queryPhraseAll() = phraseRepository.findAll().map {
        phraseMapper.toDomain(it)!!
    }

    override fun queryPhraseAllByPointTypeAndStandardPoint(type: PointType, point: Int): List<Phrase> {
        return phraseRepository.findAllByTypeAndStandardBetween(
            type = type,
            previousStandard = point,
            standard = point.plus(10)
        ).map {
            phraseMapper.toDomain(it)!!
        }
    }
}
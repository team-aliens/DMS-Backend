package team.aliens.dms.persistence.point

import org.springframework.stereotype.Component
import team.aliens.dms.domain.point.model.Phrase
import team.aliens.dms.domain.point.model.PointType
import team.aliens.dms.domain.point.spi.PhrasePort
import team.aliens.dms.persistence.point.mapper.PhraseMapper
import team.aliens.dms.persistence.point.repository.PhraseJpaRepository
import kotlin.math.absoluteValue

@Component
class PhrasePersistenceAdapterStudent(
    private val phraseRepository: PhraseJpaRepository,
    private val phraseMapper: PhraseMapper
) : PhrasePort {

    override fun queryPhraseAllByPointTypeAndStandardPoint(type: PointType, point: Int): List<Phrase> {
        val standard = if (point >= 10) {
            /**
             * 무조건 일의 자리만 나오는 상수
             **/
            point - ((10 - point).absoluteValue % 10)
        } else 0

        return phraseRepository.findAllByTypeAndStandard(
            type = type,
            standard = standard
        ).map {
            phraseMapper.toDomain(it)!!
        }
    }
}
